package com.faithie.ipptapp.posedetector

import android.content.Context
import android.media.Image
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.faithie.ipptapp.posedetector.classification.ExerciseType
import com.faithie.ipptapp.posedetector.classification.PoseClassifierProcessor
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.PoseDetector
import com.google.mlkit.vision.pose.PoseLandmark
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class PoseDetectionAnalyser(
    private val context: Context,
    private val onDetectPose: (List<PoseLandmark>) -> Unit,
    private val onClassifiedPose: (Int) -> Unit
): ImageAnalysis.Analyzer {

    private val TAG = "PoseDetectionAnalyser"

    private val options = PoseDetectorOptions.Builder()
        .setDetectorMode(PoseDetectorOptions.STREAM_MODE)
        .build()
    private val poseDetector: PoseDetector = PoseDetection.getClient(options)
//    val poseClassifierProcessor: PoseClassifierProcessor = PoseClassifierProcessor(context, true)
    private lateinit var poseClassifierProcessor: PoseClassifierProcessor

    init {
        initialisePoseClassifierProcessor(context)
    }

    @OptIn(ExperimentalGetImage::class) override fun analyze(imageProxy: ImageProxy) {
        val mediaImage: Image? = imageProxy.image
        if (mediaImage != null){
            val inputImage: InputImage = InputImage.fromMediaImage(
                mediaImage, imageProxy.imageInfo.rotationDegrees
            )
            poseDetector.process(inputImage)
                .addOnSuccessListener { result ->
                    // Get all the poselandmarks from the result of the poseDetector
                    val detectedPoses: List<PoseLandmark> = result.allPoseLandmarks

                    // Check that there are detected landmarks
                    if(detectedPoses != null){
                        // Call the callback function that updates the pose landmarks in the UI
                        onDetectPose(detectedPoses)
                        if (::poseClassifierProcessor.isInitialized) {
                            var poseClassificationRes: Int = poseClassifierProcessor.getPoseResult(result)
                            if (poseClassificationRes != null){
                                onClassifiedPose(poseClassificationRes)
                            }
                        }
                    }else{
//                        Log.d("PoseDetector", "detected poses is null")
                    }
                }.addOnCompleteListener {
                    imageProxy.close()
                }
        }
    }
    private fun initialisePoseClassifierProcessor(context: Context) {
//        Thread {
//            poseClassifierProcessor = PoseClassifierProcessor(context, true)
//        }.start()
        CoroutineScope(Dispatchers.IO).launch {
            poseClassifierProcessor = PoseClassifierProcessor(context, true, ExerciseType.PUSHUP)
        }
    }
}