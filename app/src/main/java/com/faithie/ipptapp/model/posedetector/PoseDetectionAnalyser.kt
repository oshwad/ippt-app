package com.faithie.ipptapp.model.posedetector

import android.content.Context
import android.media.Image
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.compose.runtime.State
import com.faithie.ipptapp.model.posedetector.classification.PoseClassifierProcessor
import com.faithie.ipptapp.model.posedetector.repcounting.ExerciseType
import com.faithie.ipptapp.model.posedetector.repcounting.PushUpExercise
import com.faithie.ipptapp.model.posedetector.repcounting.SitUpExercise
import com.faithie.ipptapp.model.posedetector.repcounting.ValidationResult
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.PoseDetector
import com.google.mlkit.vision.pose.PoseLandmark
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PoseDetectionAnalyser(
    private val context: Context,
    private val getImageDim: (ImageProxy) -> Unit,
    private val onDetectPose: (List<PoseLandmark>) -> Unit,
    private val onClassifiedPose: (Int, List<ValidationResult>) -> Unit,
    private var currentExercise: State<ExerciseType>,
    private val isExerciseInProgress: State<Boolean>
): ImageAnalysis.Analyzer {

    private val TAG = "PoseDetectionAnalyser"

    private var curClassification: String = ""

    private val options = PoseDetectorOptions.Builder()
        .setDetectorMode(PoseDetectorOptions.STREAM_MODE)
        .build()
    private val poseDetector: PoseDetector = PoseDetection.getClient(options)
    private lateinit var poseClassifierProcessor: PoseClassifierProcessor

    init {
        initialisePoseClassifierProcessor(context)
    }

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage: Image? = imageProxy.image
        if (mediaImage != null){
            getImageDim(imageProxy)
//            Log.d(TAG, "Image dimensions: width=${imageProxy.width}, height=${imageProxy.height}")
            val inputImage: InputImage = InputImage.fromMediaImage(
                mediaImage, imageProxy.imageInfo.rotationDegrees
            )
            poseDetector.process(inputImage)
                .addOnSuccessListener { result ->
                    val detectedPoses: List<PoseLandmark> = result.allPoseLandmarks

                    if(detectedPoses != null){
                        // Call the callback function that updates the pose landmarks in the UI
                        onDetectPose(detectedPoses)

                        if(::poseClassifierProcessor.isInitialized) {
//                            Log.d(TAG, "exercise in progress")
                            classifyAndCountReps(result)
                        }
                    }
                }.addOnCompleteListener {
                    imageProxy.close()
                }
        }
    }
    private fun initialisePoseClassifierProcessor(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            poseClassifierProcessor = PoseClassifierProcessor(context, true)
        }
    }

    private fun classifyAndCountReps(pose: Pose) {
//        Log.d(TAG, "classifying and counting reps")
        val classificationLabel: String?
        val numReps: Int
        var validationResults: List<ValidationResult> = emptyList()

        if (isExerciseInProgress.value) {
            classificationLabel = poseClassifierProcessor.getClassifiedPose(pose, currentExercise.value)
            if (classificationLabel != null){
                if(curClassification != classificationLabel){
                    Log.d(TAG, "classified pose is: $classificationLabel")
                    curClassification = classificationLabel
                }
                if (currentExercise.value is PushUpExercise) {
                    numReps = poseClassifierProcessor.pushUpExercise.validateSequence(pose, classificationLabel)
                    validationResults = poseClassifierProcessor.pushUpExercise.validationResults
                } else {
                    numReps = poseClassifierProcessor.sitUpExercise.validateSequence(pose, classificationLabel)
                }

                onClassifiedPose(numReps, validationResults)
            }
        }
    }

    fun resetReps() {
        if (::poseClassifierProcessor.isInitialized) {
            poseClassifierProcessor.resetReps()
        }
    }
}