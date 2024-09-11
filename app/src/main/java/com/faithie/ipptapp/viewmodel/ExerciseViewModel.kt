package com.faithie.ipptapp.viewmodel

import android.app.Application
import androidx.camera.core.CameraSelector
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.faithie.ipptapp.posedetector.PoseDetectionAnalyser
import com.google.mlkit.vision.pose.PoseLandmark
import java.util.concurrent.Executors

class ExerciseViewModel(private val application: Application) : AndroidViewModel(application = application) {

    var executor = mutableStateOf(Executors.newSingleThreadExecutor())
    var analyser = mutableStateOf<PoseDetectionAnalyser>(
        PoseDetectionAnalyser(
        onDetectPose = { poseLandmarks ->
            // Update the LiveData with the detected pose landmarks
            poseLandmarksLive.value = poseLandmarks
            _poseLandmarks.value = poseLandmarks
        },
        getApplication(),
    )
    )

    var poseLandmarksLive = mutableStateOf<List<PoseLandmark>>(emptyList())


    var controller = mutableStateOf(LifecycleCameraController(getApplication()).apply {
        setEnabledUseCases( // Enable the intended use cases to be used (picture, video, analysis)
//            CameraController.IMAGE_CAPTURE or
//            CameraController.VIDEO_CAPTURE or
            CameraController.IMAGE_ANALYSIS
        )
        setImageAnalysisAnalyzer(
            executor.value,
            analyser.value
        )
        cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
    })

    private var _poseLandmarks = mutableStateOf<List<PoseLandmark>>(emptyList())
    val poseLandmarksLiveData: State<List<PoseLandmark>> get() = _poseLandmarks

}
