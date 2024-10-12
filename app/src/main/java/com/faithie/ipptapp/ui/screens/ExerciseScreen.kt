package com.faithie.ipptapp.ui.screens

import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.faithie.ipptapp.ui.component.CameraPreview
import com.faithie.ipptapp.ui.component.CameraPreviewWithGraphicOverlay
import com.faithie.ipptapp.ui.component.CountdownTimer
import com.faithie.ipptapp.ui.component.PoseGraphicOverlay
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

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE

    LaunchedEffect(Unit) {
        viewModel.resetExerciseViewModel()
    }

    LaunchedEffect(isWorkoutCompleted) {
        if (isWorkoutCompleted) {
            navController.navigate(Screens.ExerciseResults.route)
        }
    }

    BackHandler(enabled = (isExerciseInProgress || !isWorkoutCompleted)) {
        showExitDialog = true
    }

    Log.d(TAG, "imageWidth: ${viewModel.imageWidth.value}, imageHeight: ${viewModel.imageHeight.value}")
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
                viewModel.setExerciseInProgress(true)
                isStartPressed = false
            }
        )

    }
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = currentExercise.name,
            style = MaterialTheme.typography.headlineSmall
        )

        Text(text = "Time left: $timer seconds")

        Box(
            modifier = Modifier.fillMaxSize()
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
                        navController.popBackStack()  // Navigate back or exit
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
