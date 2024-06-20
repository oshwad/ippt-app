package com.faithie.ipptapp.feature_posedetector

import android.content.Context
import android.media.Image
import android.util.Log
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
    private val onPoseDetect: (List<PoseLandmark>) -> Unit,
    private val context: Context
): ImageAnalysis.Analyzer {
    private val options = PoseDetectorOptions.Builder()
        .setDetectorMode(PoseDetectorOptions.STREAM_MODE)
        .build()
    private val poseDetector: PoseDetector = PoseDetection.getClient(options)
    @OptIn(ExperimentalGetImage::class) override fun analyze(image: ImageProxy) {
//        Log.d("analyser", "running")
        val mediaImage: Image? = image.image?:null
        if (mediaImage != null) {
            val inputImage: InputImage = InputImage.fromMediaImage(
                mediaImage, image.imageInfo.rotationDegrees
            )
            poseDetector.process(inputImage)
                .addOnSuccessListener { results ->
//                    Log.d("PDA","$results")
                    val poses: List<PoseLandmark> = results.allPoseLandmarks
                    if (poses.isNotEmpty()) {
                        Log.d("PDA","$poses")
                        onPoseDetect(poses)
                    }
                }
                .addOnCompleteListener() {
                    image.close()
                }
        }
    }

}