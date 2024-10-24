package com.faithie.ipptapp.ui.screens

import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.faithie.ipptapp.model.posedetector.repcounting.PushUpExercise
import com.faithie.ipptapp.ui.component.CameraPreviewWithGraphicOverlay
import com.faithie.ipptapp.ui.component.CountdownTimer
import com.faithie.ipptapp.viewmodel.ExerciseViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExerciseScreen(
    navController: NavHostController,
    viewModel: ExerciseViewModel,
) {
    val TAG = "ExerciseScreen"
    val timer by viewModel.timer
    val currentExercise by viewModel.currentExercise
    val isExerciseInProgress by viewModel.isExerciseInProgress
    val isWorkoutCompleted by viewModel.isWorkoutCompleted

    var isStartPressed by remember { mutableStateOf(false) }
    var showExitDialog by remember { mutableStateOf(false) }

//    val configuration = LocalConfiguration.current
//    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE

    var numReps by remember { mutableStateOf(0) }
    if (currentExercise.name == PushUpExercise().name) {
        numReps = viewModel.numRepsPushUp.value
//        Log.d(TAG, "num reps pushup view model ${viewModel.numRepsPushUp.value}")
//        Log.d(TAG, "num reps pushup observed from view model state $numReps")
    }
    else {
        numReps = viewModel.numRepsSitUp.value
//        Log.d(TAG, "num reps situp view model ${viewModel.numRepsSitUp.value}")
//        Log.d(TAG, "num reps situp observed from view model state $numReps")
    }

    LaunchedEffect(Unit) {
        Log.d(TAG, "reset exercise view model")
        viewModel.resetExerciseViewModel()
    }

    LaunchedEffect(isWorkoutCompleted) {
        if (isWorkoutCompleted) {
            navController.navigate(Screens.WorkoutResults.route)
        }
    }

    BackHandler {
        showExitDialog = true
    }

//    Log.d(TAG, "imageWidth: ${viewModel.imageWidth.value}, imageHeight: ${viewModel.imageHeight.value}")
    CameraPreviewWithGraphicOverlay(
        controller = viewModel.controller.value,
        posePositions = viewModel.poseLandmarks.value,
        imageDims = mutableStateOf(Pair(viewModel.imageWidth.value, viewModel.imageHeight.value))
    )
//    CameraPreview(controller = viewModel.controller.value, onCameraChanges = { x, y -> })
//    PoseGraphicOverlay(controller = viewModel.controller.value, posePositions = viewModel.poseLandmarks.value)



    if (isStartPressed) {
        CountdownTimer(
            onCountdownFinished = {
                viewModel.startWorkout()
                isStartPressed = false
            }
        )

    }
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(modifier = Modifier){
            Column {
                Text(
                    text = currentExercise.name,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White
                )
                Text(
                    text = "Time left: $timer seconds",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )
            }
        }

        if (isExerciseInProgress) {
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = "$numReps",
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 250.sp,
                color = Color.White
            )
        }

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
                    isStartPressed = true
                },
                enabled = !isExerciseInProgress,
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Text(
                    text = "Start",
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("End Workout") },
            text = { Text("Are you sure you want to end the workout?") },
            confirmButton = {
                Button(
                    onClick = {
                        showExitDialog = false
                        viewModel.stopTimer()
                        viewModel.resetExerciseViewModel()
                        navController.popBackStack()
                    }
                ) {
                    Text(
                        text = "Yes",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            },
            dismissButton = {
                Button(
                    onClick = { showExitDialog = false }
                ) {
                    Text(
                        text = "No",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        )
    }
}
