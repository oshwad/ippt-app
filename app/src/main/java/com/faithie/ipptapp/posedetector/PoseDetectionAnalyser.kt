package com.faithie.ipptapp.posedetector

import android.content.Context
import android.media.Image
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.PoseDetector
import com.google.mlkit.vision.pose.PoseLandmark
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions


class PoseDetectionAnalyser(
    private val onDetectPose: (List<PoseLandmark>) -> Unit,
    private val context: Context
): ImageAnalysis.Analyzer {

    private val TAG = "PoseDetectionAnalyser"

    private val options = PoseDetectorOptions.Builder()
        .setDetectorMode(PoseDetectorOptions.STREAM_MODE)
        .build()
    private val poseDetector: PoseDetector = PoseDetection.getClient(options)

    @OptIn(ExperimentalGetImage::class) override fun analyze(imageProxy: ImageProxy) {
        val mediaImage: Image? = imageProxy.image
        if (mediaImage != null){
            val inputImage: InputImage = InputImage.fromMediaImage(
                mediaImage, imageProxy.imageInfo.rotationDegrees
            )
            poseDetector.process(inputImage)
                .addOnSuccessListener { results ->
                    // Get all the poselandmarks from the result of the poseDetector
                    val detectedPoses: List<PoseLandmark> = results.allPoseLandmarks

                    // Check that there are detected landmarks
                    if(detectedPoses != null){
                        // Call the callback function that updates the pose landmarks in the UI
                        onDetectPose(detectedPoses)
                    }else{
//                        Log.d("PoseDetector", "detected poses is null")
                    }
                }.addOnCompleteListener {
                    imageProxy.close()
                }
        }
    }

}