package com.faithie.ipptapp.viewmodel

import android.app.Application
import android.media.AudioManager
import android.media.ToneGenerator
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.faithie.ipptapp.model.posedetector.PoseDetectionAnalyser
import com.faithie.ipptapp.model.posedetector.repcounting.ExerciseType
import com.faithie.ipptapp.model.posedetector.repcounting.PushUpExercise
import com.faithie.ipptapp.model.posedetector.repcounting.SitUpExercise
import com.faithie.ipptapp.model.posedetector.repcounting.ValidationResult
import com.google.mlkit.vision.pose.PoseLandmark
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

class DevExerciseViewModel(application: Application): AndroidViewModel(application = application) {
    private val TAG = "DevExerciseViewModel"

    private var executor = mutableStateOf(Executors.newSingleThreadExecutor())

    private var _poseLandmarks = mutableStateOf<List<PoseLandmark>>(emptyList())
    val poseLandmarks: State<List<PoseLandmark>> get() = _poseLandmarks

    private val _currentExercise = mutableStateOf<ExerciseType>(PushUpExercise())
    val currentExercise: State<ExerciseType> get() = _currentExercise
    val isExerciseInProgress: State<Boolean> get() = _isExerciseInProgress
    private val _isExerciseInProgress = mutableStateOf(false)
    private val _numRepsPushUp = mutableIntStateOf(0)
    val numRepsPushUp: State<Int> get() = _numRepsPushUp
    private val _numRepsSitUp = mutableIntStateOf(0)
    val numRepsSitUp: State<Int> get() = _numRepsSitUp
    private val _curClassLabel = mutableStateOf("")
    val curClassLabel: State<String> get() = _curClassLabel

    private val _validationResults = mutableStateOf(emptyList<ValidationResult>())
    val validationResults: State<List<ValidationResult>> get() = _validationResults
    val tg = ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100)

    private var analyser = mutableStateOf<PoseDetectionAnalyser>(
        PoseDetectionAnalyser(
            getApplication(),
            getImageDim = { _ ->
            },
            onDetectPose = { poseLandmarks ->
                // Update the live data with the detected pose landmarks
                _poseLandmarks.value = poseLandmarks
            },
            onClassifiedPose = { reps, classLabel, validationRes ->
                updateReps(reps)
                _curClassLabel.value = classLabel
                _validationResults.value = validationRes
                Log.d(TAG, "${validationResults.value}")
            },
            currentExercise = _currentExercise,
            isExerciseInProgress
        )
    )

    var controller = mutableStateOf(LifecycleCameraController(getApplication()).apply {
        setEnabledUseCases(CameraController.IMAGE_ANALYSIS)
        setImageAnalysisAnalyzer(
            executor.value,
            analyser.value
        )
        cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    })

    fun startWorkout() {
        _isExerciseInProgress.value = true
    }

    fun resetExerciseViewModel() {
        analyser.value.resetReps()
        _poseLandmarks.value = emptyList()
        _isExerciseInProgress.value = false
        _numRepsPushUp.value = 0
        _numRepsSitUp.value = 0
        Log.d(TAG, "resetting view model")
    }

    private fun updateReps(reps: Int) {
        if (_currentExercise.value is PushUpExercise) {
            _numRepsPushUp.value = reps // Store reps for Push-Up
        } else if (_currentExercise.value is SitUpExercise) {
            _numRepsSitUp.value = reps // Store reps for Sit-Up
        }
    }

    fun setExerciseType(exerciseType: ExerciseType) {
        _currentExercise.value = exerciseType
    }
}