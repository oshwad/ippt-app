package com.faithie.ipptapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.faithie.ipptapp.posedetector.PoseDetectionAnalyser
import com.google.mlkit.vision.pose.PoseLandmark
import java.util.concurrent.Executors

class ExerciseViewModel(application: Application) :
    AndroidViewModel(application = application) {

    private val TAG = "ExerciseViewModel"

    private var executor = mutableStateOf(Executors.newSingleThreadExecutor())
    private var _poseLandmarks = mutableStateOf<List<PoseLandmark>>(emptyList())
    val poseLandmarks: State<List<PoseLandmark>> get() = _poseLandmarks

    private var analyser = mutableStateOf<PoseDetectionAnalyser>(
        PoseDetectionAnalyser(
            getApplication(),
            onDetectPose = { poseLandmarks ->
                // Update the live data with the detected pose landmarks
                _poseLandmarks.value = poseLandmarks
            },
            onClassifiedPose = {
//                for (result in it) {
//                    Log.d(TAG, result)  // This will include "pushups : X reps" or "squats : X reps"
//                }
            }
        )
    )

    var controller = mutableStateOf(LifecycleCameraController(getApplication()).apply {
        setEnabledUseCases(CameraController.IMAGE_ANALYSIS)
        setImageAnalysisAnalyzer(
            executor.value,
            analyser.value
        )
        cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
    })
}
