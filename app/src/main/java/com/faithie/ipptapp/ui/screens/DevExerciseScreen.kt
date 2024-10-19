package com.faithie.ipptapp.ui.screens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
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
import com.faithie.ipptapp.model.posedetector.repcounting.SitUpExercise
import com.faithie.ipptapp.ui.component.CameraPreviewWithGraphicOverlay
import com.faithie.ipptapp.viewmodel.DevExerciseViewModel

@Composable
fun DevExerciseScreen(
    navController: NavHostController,
    viewModel: DevExerciseViewModel
) {
    val TAG = "DevExerciseScreen"
    val currentExercise by viewModel.currentExercise
    val isExerciseInProgress by viewModel.isExerciseInProgress
    val validationResults by viewModel.validationResults
    val curClassLabel by viewModel.curClassLabel
    Log.d(TAG, "$validationResults")

    var expanded by remember { mutableStateOf(false) }

    var numReps by remember { mutableStateOf(0) }
    if (currentExercise.name == PushUpExercise().name) {
        numReps = viewModel.numRepsPushUp.value
    }
    else {
        numReps = viewModel.numRepsSitUp.value
    }

    LaunchedEffect(Unit) {
        viewModel.resetExerciseViewModel()
    }

    BackHandler {
        viewModel.resetExerciseViewModel()
        navController.popBackStack()
    }

    CameraPreviewWithGraphicOverlay(
        controller = viewModel.controller.value,
        posePositions = viewModel.poseLandmarks.value,
        imageDims = mutableStateOf(Pair(0, 0))
    )

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(modifier = Modifier){
            Column {
                Box(modifier = Modifier.wrapContentSize(Alignment.TopStart)) {
                    Button(onClick = { expanded = true }) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = currentExercise.name, style = MaterialTheme.typography.labelMedium)
                            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Dropdown icon")
                        }
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(text = "Push Up") },
                            onClick = {
                                viewModel.setExerciseType(PushUpExercise())
                                viewModel.resetExerciseViewModel()
                                expanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(text = "Sit Up") },
                            onClick = {
                                viewModel.setExerciseType(SitUpExercise())
                                viewModel.resetExerciseViewModel()
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        if (curClassLabel.isNotEmpty()) {
            Text(
                "current classification: $curClassLabel",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White
            )
        }
        if (validationResults.isNotEmpty()) {
            Text(
                "validation result from: ${validationResults[0].classification}",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White
            )
        }
        if(currentExercise is PushUpExercise) {
            validationResults.forEach {
                Text(
                    "${it.location} - valid: ${it.validAngles}, angle: ${it.angle}",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White
                )
            }
        } else if (currentExercise is SitUpExercise) {
            validationResults.forEach {
                Text(
                    "${it.location} - valid: ${it.validAngles}, angle: ${it.angle}\nleftHandEar - valid: ${it.validHandEarDist}, handEarDist: ${it.handEarDist}",
                    style = MaterialTheme.typography.labelSmall,
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
                    viewModel.startWorkout()
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
}