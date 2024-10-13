package com.faithie.ipptapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.faithie.ipptapp.ui.component.DayCountdown
import com.faithie.ipptapp.ui.component.StartExerciseCard
import com.faithie.ipptapp.ui.component.WorkoutResultsTable
import com.faithie.ipptapp.viewmodel.RecordsViewModel
import com.faithie.ipptapp.viewmodel.UserViewModel
import java.time.LocalDate

@Composable
fun HomeScreen(
    navController: NavHostController,
    userViewModel: UserViewModel,
    recordsViewModel: RecordsViewModel
) {
    var userName by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        userName = userViewModel.getUser()?.name ?: ""
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    )
    {
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Hello, $userName",
            style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        StartExerciseCard(
            onClick = {
                navController.navigate(Screens.Exercise.route)
            }
        )

        Spacer(modifier = Modifier.height(36.dp))

        Text(text = "Last Exercise",
            style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        val mostRecentWorkout = recordsViewModel.mostRecentWorkout
        if (mostRecentWorkout != null) {
            WorkoutResultsTable(listOf(mostRecentWorkout.value!!))
        } else {
            WorkoutResultsTable(emptyList())
        }

        Spacer(modifier = Modifier.height(36.dp))
        Text(text = "Upcoming IPPT",
            style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        var nextIpptDate = remember {
            mutableStateOf<LocalDate?>(null)
        }
        LaunchedEffect(Unit) {
            val user = userViewModel.getUser()
            user?.let {
                nextIpptDate.value = it.nextIpptDate
            }
        }
        DayCountdown(nextIpptDate = nextIpptDate.value)
    }
}