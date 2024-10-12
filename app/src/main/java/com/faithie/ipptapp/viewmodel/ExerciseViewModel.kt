package com.faithie.ipptapp.viewmodel

import android.app.Application
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.faithie.ipptapp.data.WorkoutDatabase
import com.faithie.ipptapp.data.WorkoutResult
import com.faithie.ipptapp.posedetector.PoseDetectionAnalyser
import com.faithie.ipptapp.posedetector.repcounting.ExerciseType
import com.faithie.ipptapp.posedetector.repcounting.PushUpExercise
import com.faithie.ipptapp.posedetector.repcounting.SitUpExercise
import com.google.mlkit.vision.pose.PoseLandmark
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.concurrent.Executors

class ExerciseViewModel(application: Application) :
    AndroidViewModel(application = application) {

    private val TAG = "ExerciseViewModel"
    private val WORKOUT_TIME = 30

    private var executor = mutableStateOf(Executors.newSingleThreadExecutor())

    private var _poseLandmarks = mutableStateOf<List<PoseLandmark>>(emptyList())
    val poseLandmarks: State<List<PoseLandmark>> get() = _poseLandmarks

    private val _currentExercise = mutableStateOf<ExerciseType>(PushUpExercise())
    val currentExercise: State<ExerciseType> get() = _currentExercise

    private val _timer = mutableIntStateOf(WORKOUT_TIME)
    val timer: State<Int> get() = _timer
    val isExerciseInProgress: State<Boolean> get() = _isExerciseInProgress
    private val _isExerciseInProgress = mutableStateOf(false)
    val isWorkoutCompleted: State<Boolean> get() = _isWorkoutCompleted
    private val _isWorkoutCompleted = mutableStateOf(false)
    private val _numRepsPushUp = mutableIntStateOf(0)
    val numRepsPushUp: State<Int> get() = _numRepsPushUp
    private val _numRepsSitUp = mutableIntStateOf(0)
    val numRepsSitUp: State<Int> get() = _numRepsSitUp
    private val workoutResultDao = WorkoutDatabase.getDatabase(application).workoutResultDao()

    private var _imageWidth = mutableStateOf(0)
    val imageWidth: State<Int> get() = _imageWidth
    private var _imageHeight = mutableStateOf(0)
    val imageHeight: State<Int> get() = _imageHeight
    val tg = ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100)

    private var analyser = mutableStateOf<PoseDetectionAnalyser>(
        PoseDetectionAnalyser(
            getApplication(),
            getImageDim = { imageProxy ->
                // Update image dimensions in the ViewModel
                _imageWidth.value = imageProxy.width
                _imageHeight.value = imageProxy.height
                Log.d(TAG, "imagewidth: ${imageWidth.value} imageHeight: ${imageHeight.value}")
            },
            onDetectPose = { poseLandmarks ->
                // Update the live data with the detected pose landmarks
                _poseLandmarks.value = poseLandmarks
            },
            onClassifiedPose = { reps ->
                Log.d(TAG, "numReps: $reps")
                updateReps(reps)
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
        cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
    })

    @RequiresApi(Build.VERSION_CODES.O)
    fun startWorkout() {
        startTimer()
    }

    fun setExerciseInProgress(inProgress: Boolean) {
        _isExerciseInProgress.value = inProgress
    }

    fun resetExerciseViewModel() {
        _poseLandmarks.value = emptyList()
        _currentExercise.value = PushUpExercise()
        _timer.value = WORKOUT_TIME
        _isExerciseInProgress.value = false
        _isWorkoutCompleted.value = false
        _numRepsPushUp.value = 0
        _numRepsSitUp.value = 0
    }

    private var workoutJob: Job? = null

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startTimer() {
        workoutJob = CoroutineScope(Dispatchers.Main).launch {
            for (i in WORKOUT_TIME downTo 0) {
                _timer.value = i
                delay(1000L)
            }
            tg.startTone(ToneGenerator.TONE_CDMA_ONE_MIN_BEEP)
            onCompleteExercise()
        }
    }

    // This function can be used to stop the timer when leaving the screen
    fun stopTimer() {
        workoutJob?.cancel()  // Cancel the timer coroutine
    }

    override fun onCleared() {
        super.onCleared()
        stopTimer()  // Cancel the coroutine when ViewModel is cleared
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun onCompleteExercise() {
        _isExerciseInProgress.value = false
        _poseLandmarks.value = emptyList()
        _timer.value = WORKOUT_TIME
        if (_currentExercise.value is PushUpExercise) {
            _currentExercise.value = SitUpExercise()
        } else {
            // Current exercise is situp
            if (!_isExerciseInProgress.value) { // situp exercise complete
                onCompleteWorkout(_numRepsPushUp.value, _numRepsSitUp.value)
            }
        }
    }

    private fun updateReps(reps: Int) {
        if (_currentExercise.value is PushUpExercise) {
            _numRepsPushUp.value = reps // Store reps for Push-Up
        } else if (_currentExercise.value is SitUpExercise) {
            _numRepsSitUp.value = reps // Store reps for Sit-Up
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun onCompleteWorkout(pushUpReps: Int, sitUpReps: Int) {
        _isWorkoutCompleted.value = true

        val result = WorkoutResult(
            pushUpReps = pushUpReps,
            sitUpReps = sitUpReps,
            date = LocalDateTime.now()
        )

        CoroutineScope(Dispatchers.IO).launch {
            workoutResultDao.insertResult(result)
            Log.d(TAG, "Workout database: " + workoutResultDao.getAllResults())
        }
    }
}
