package com.faithie.ipptapp.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.faithie.ipptapp.viewmodel.ExerciseViewModel

@Composable
fun WorkoutResultsScreen(
    navController: NavHostController,
    viewModel: ExerciseViewModel
) {
    val TAG = "WorkoutResultsScreen"
    val context = LocalContext.current

    BackHandler {
        viewModel.resetExerciseViewModel()
        Log.d(TAG, "reset exercise view model")
        navController.popBackStack(Screens.Home.route, inclusive = false)
    }

    LaunchedEffect(Unit) {
        Toast.makeText(context, "Workout saved successfully", Toast.LENGTH_SHORT).show()
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
    }
}