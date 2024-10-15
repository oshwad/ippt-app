package com.faithie.ipptapp.viewmodel

import android.app.Application
import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.faithie.ipptapp.model.posedetector.repcounting.ExerciseType
import com.faithie.ipptapp.model.posedetector.repcounting.PushUpExercise
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark
import com.google.mlkit.vision.common.PointF3D
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import java.io.FileWriter
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class PoseTrainingViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = "PoseTrainingViewModel"
    private val context: Context = getApplication<Application>().applicationContext

    // Initialize pose detector
    private val poseDetectorOptions = PoseDetectorOptions.Builder()
        .setDetectorMode(PoseDetectorOptions.SINGLE_IMAGE_MODE)
        .build()
    private val poseDetector = PoseDetection.getClient(poseDetectorOptions)

    // Holds the currently selected exercise type
    private val _selectedExerciseType = MutableStateFlow(PushUpExercise())
    val selectedExerciseType: StateFlow<ExerciseType> = _selectedExerciseType

    // Function to update the selected exercise type
    fun setExerciseType(exerciseType: ExerciseType) {
        _selectedExerciseType.value = exerciseType as PushUpExercise
    }

    init {
        viewModelScope.launch {
            val poseList = getPosesFromAssets(context)
            Log.d(TAG, "init: poses obtained from postList $poseList")
            for (pose in poseList) {
                Log.d(TAG, "iterating over poseList")
//                savePoseToCSV(context, pose, _selectedExerciseType)
            }
        }
    }
//    private val _isStreamMode = MutableStateFlow(true)
//    val isStreamMode: StateFlow<Boolean> = _isStreamMode
//    fun setTrainingMode(isStreamMode: Boolean) {
//        _isStreamMode.value = isStreamMode
//    }

    suspend fun getPosesFromAssets(context: Context): List<Pose> {
        val assetManager = context.assets
        val inputImages = mutableListOf<InputImage>()
        val poseList = mutableListOf<Pose>()

        try {
            // List all files in the assets directory
            val files = assetManager.list("") // Change to specific folder if needed

            // Define the image extensions you want to filter
            val imageExtensions = listOf("jpg", "jpeg", "png")

            files?.forEach { file ->
                // Check if the file has an image extension
                if (imageExtensions.any { file.endsWith(it, ignoreCase = true) }) {

                    // Load the bitmap from the assets
                    val inputStream = assetManager.open(file)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    val image = InputImage.fromBitmap(bitmap, 0) // Rotation is 0

                    inputImages.add(image) // Add the file to the list if it matches
                }
            }

            inputImages.forEach { image ->
                val pose = processImage(image)
                poseList.add(pose)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return poseList
    }

    // Helper function to process the image and await the result
    private suspend fun processImage(image: InputImage): Pose =
        suspendCancellableCoroutine { continuation ->
            poseDetector.process(image)
                .addOnSuccessListener { pose ->
                    continuation.resume(pose) // Resume coroutine with pose result
                }
                .addOnFailureListener { e ->
                    continuation.resumeWithException(e) // Resume with an error
                }
        }

    fun savePoseToCSV(context: Context, pose: Pose, exerciseType: MutableStateFlow<ExerciseType>) {
        Log.d(TAG, "saving poses to csv file")

        // Prepare the CSV file
        val csvFile = File(context.getExternalFilesDir(null), "poses.csv")

        try {
            // Create the file if it doesn't exist and write the header
            if (!csvFile.exists()) {
                Log.d(TAG, "poses.csv file does not exist")
                csvFile.createNewFile()
//            FileWriter(csvFile, true).use { writer ->
//                // Write header row
//                writer.append("RowName,ExerciseType")
//                for (i in 0 until 33) {
//                    writer.append(",X$i,Y$i,Z$i")
//                }
//                writer.append("\n")
//            }
            }

            // Prepare data row
            val rowName = System.currentTimeMillis().toString() // or format as needed
            val dataRow = StringBuilder("")

//        val landmarks: List<PoseLandmark> = pose.allPoseLandmarks
//        for (landmark in landmarks) {
//            if (landmark != null) {
//                val position3D: PointF3D = landmark.position3D
//                dataRow.append(",${position3D.x},${position3D.y},${position3D.z}")
//            } else {
//                // If landmark is not detected, fill with default values (e.g., 0)
//                dataRow.append(",0,0,0")
//            }
//        }

            // Collect landmark data
            for (i in 0 until 33) {
                val landmark: PoseLandmark? = pose.getPoseLandmark(i)
                if (landmark != null) {
                    val position3D: PointF3D = landmark.position3D
                    dataRow.append(",${position3D.x},${position3D.y},${position3D.z}")
                } else {
                    // If landmark is not detected, fill with default values (e.g., 0)
                    dataRow.append(",0,0,0")
                }
            }

            val fullRow = "$rowName,${exerciseType.value}$dataRow"
            Log.d(TAG, "Data Row: $fullRow") // Log the data row before writing

            FileWriter(csvFile, true).use { writer ->
                writer.append(fullRow).append("\n")
            }

            verifyCSVContent(context)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun verifyCSVContent(context: Context) {
        val csvFile = File(context.getExternalFilesDir(null), "poses.csv")

        if (csvFile.exists()) {
            csvFile.forEachLine { line ->
                Log.d(TAG, "CSV Line: $line")
            }
        } else {
            Log.d(TAG, "CSV file does not exist.")
        }
    }

}


