package com.faithie.ipptapp.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.faithie.ipptapp.model.entity.User
import com.faithie.ipptapp.model.entity.WorkoutResult
import com.faithie.ipptapp.ui.component.IntDropdownMenu
import com.faithie.ipptapp.viewmodel.ExerciseViewModel
import dagger.hilt.android.internal.Contexts.getApplication
import kotlinx.coroutines.launch

@Composable
fun WorkoutResultsScreen(
    navController: NavHostController,
    viewModel: ExerciseViewModel
) {
    val TAG = "WorkoutResultsScreen"
    val context = LocalContext.current

    var selectedMinutes by remember { mutableStateOf<Int?>(null) }
    var selectedSeconds by remember { mutableStateOf<Int?>(null) }
    var runTimingError by remember { mutableStateOf(false) }
    var isDoneClicked by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    BackHandler {
        viewModel.resetExerciseViewModel()
        Log.d(TAG, "reset exercise view model")
        navController.popBackStack(Screens.Home.route, inclusive = false)
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.resetExerciseViewModel()
            Log.d(TAG, "ViewModel reset in onDispose ${viewModel.numRepsPushUp}, ${viewModel.numRepsSitUp}")
        }
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
    ){
        Text(
            text = "Workout Complete",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Push Up Reps: ${viewModel.numRepsPushUp.value}",
            style = MaterialTheme.typography.labelLarge
        )
//        Text(
//            text = "Push Up No-Count Reps: ${viewModel.numRepsPushUp.value}",
//            style = MaterialTheme.typography.bodySmall
//        )
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Sit Up Reps: ${viewModel.numRepsSitUp.value}",
            style = MaterialTheme.typography.labelLarge
        )
//        Text(
//            text = "Sit Up No-Count Reps: ${viewModel.numRepsSitUp.value}",
//            style = MaterialTheme.typography.bodySmall
//        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            modifier = Modifier.align(Alignment.Start),
            text = "Enter 2.4 km timing:",
            style = MaterialTheme.typography.labelMedium
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val minutesOptions = (6..20).toList()
            selectedMinutes = IntDropdownMenu(
                textLabel = "Minutes",
                options = minutesOptions,
                initialValue = null,
                modifier = Modifier.width(180.dp)
            )

            Spacer(modifier = Modifier.width(5.dp))

            val secondsOptions = (0..59).toList()
            selectedSeconds = IntDropdownMenu(
                textLabel = "Seconds",
                options = secondsOptions,
                initialValue = null,
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (selectedMinutes == null || selectedSeconds == null) {
            runTimingError = true
            if (isDoneClicked) {
                Text("Please select both minutes and seconds",
                    modifier = Modifier.align(Alignment.Start),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error)
            }
        } else {
            runTimingError = false
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            if (!runTimingError) {
                coroutineScope.launch {
                    val runTiming = selectedMinutes!! + selectedSeconds!! / 60f
//                    val workoutResult = WorkoutResult()
//                    viewModel.insertResult(workoutResult)
                    viewModel.insertResult(runTiming)
                    Toast.makeText(context, "Workout saved successfully", Toast.LENGTH_SHORT).show()
                }
            } else {
                isDoneClicked = true
            }
        }) {
            Text(text = "Save Workout", style = MaterialTheme.typography.labelMedium)
        }
    }
}