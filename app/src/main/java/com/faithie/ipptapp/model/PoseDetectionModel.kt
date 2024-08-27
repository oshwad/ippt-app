package com.faithie.ipptapp.model

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.PoseDetector
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions

class PoseDetectionModel {

    private val poseDetector: PoseDetector

    init {
        val options = PoseDetectorOptions.Builder()
            .setDetectorMode(PoseDetectorOptions.STREAM_MODE)
            .build()

        poseDetector = PoseDetection.getClient(options)
    }

    fun detectPose(image: Bitmap, onSuccess: (Pose) -> Unit, onFailure: (Exception) -> Unit) {
        val inputImage = InputImage.fromBitmap(image, 0)
        poseDetector.process(inputImage)
            .addOnSuccessListener { pose -> onSuccess(pose) }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun stopPoseDetection() {
        poseDetector.close()
    }
}
