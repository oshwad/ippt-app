package com.faithie.ipptapp.viewmodel

import android.graphics.Bitmap
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faithie.ipptapp.model.PoseDetectionModel
import com.google.mlkit.vision.pose.Pose
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExerciseViewModel : ViewModel() {

    private val poseDetectionModel = PoseDetectionModel()
    var repCount = 0
        private set

    private var previousPose: Pose? = null

    fun analyzeImage(imageProxy: ImageProxy) {
        val bitmap = imageProxy.toBitmap() ?: return
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                poseDetectionModel.detectPose(bitmap, { pose ->
                    if (isPushUpPose(pose)) {
                        if (previousPose == null || !isPushUpPose(previousPose!!)) {
                            repCount++
                        }
                    }
                    previousPose = pose
                    imageProxy.close()
                }, { exception ->
                    imageProxy.close()
                })
            }
        }
    }

    private fun isPushUpPose(pose: Pose): Boolean {
        // Implement logic to detect if the pose corresponds to a push-up
        return true
    }

    override fun onCleared() {
        super.onCleared()
        poseDetectionModel.stopPoseDetection()
    }
}
