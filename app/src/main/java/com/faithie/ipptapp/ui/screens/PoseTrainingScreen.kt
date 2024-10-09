package com.faithie.ipptapp.ui.screens

import android.util.Log
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
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.faithie.ipptapp.posedetector.repcounting.PushUpExercise
import com.faithie.ipptapp.posedetector.repcounting.SitUpExercise
import com.faithie.ipptapp.viewmodel.PoseTrainingViewModel

@Composable
fun PoseTrainingScreen(
    navController: NavController,
    viewModel: PoseTrainingViewModel
) {
    val TAG = "PoseTrainingScreen"
    DisposableEffect(Unit) {
        Log.d("HelloScreen", "Hello")

        onDispose {
            Log.d("HelloScreen", "Goodbye")
        }
    }

    // Local state to track if the dropdown is expanded
    var expanded by remember { mutableStateOf(false) }

    // Track the selected exercise type from the ViewModel
    val selectedExerciseType by viewModel.selectedExerciseType.collectAsState()

    val exerciseOptions = listOf(PushUpExercise(), SitUpExercise())

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Show currently selected exercise type
        Text(text = "Current Training: ${selectedExerciseType.name}")

        Row {
            Box(modifier = Modifier
                .wrapContentSize(Alignment.TopStart)) {
                Button(onClick = { expanded = true }) {
                    Row {
                        Text(text = "Pose", style = MaterialTheme.typography.labelMedium)
                        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Dropdown icon")
                    }
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    exerciseOptions.forEach { exercise ->
                        DropdownMenuItem(
                            text = { Text(text = exercise.getExerciseName()) },
                            onClick = {
                                viewModel.setExerciseType(exercise) // Update the ViewModel
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}
