package com.faithie.ipptapp.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.faithie.ipptapp.viewmodel.ExerciseViewModel

@Composable
fun ExerciseResultsScreen(
    navController: NavHostController,
    viewModel: ExerciseViewModel
) {
    BackHandler {
        viewModel.resetExerciseViewModel()
        navController.navigate(Screens.Home.route)
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        Text(
            text = "Workout Complete",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Push Up Reps: ${viewModel.getNumRepsPushUp()}",
            style = MaterialTheme.typography.labelLarge
        )
        Text(
            text = "Push Up No-Count Reps: ${viewModel.getNumRepsPushUp()}",
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Sit Up Reps: ${viewModel.getNumRepsSitUp()}",
            style = MaterialTheme.typography.labelLarge
        )
        Text(
            text = "Sit Up No-Count Reps: ${viewModel.getNumRepsSitUp()}",
            style = MaterialTheme.typography.bodySmall
        )
    }
}