package com.faithie.ipptapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.faithie.ipptapp.R
import com.faithie.ipptapp.data.WorkoutResult
import com.faithie.ipptapp.ui.component.WorkoutResultsTable
import com.faithie.ipptapp.viewmodel.RecordsViewModel
import com.faithie.ipptapp.viewmodel.UserViewModel

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
            .padding(25.dp),
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

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Last Exercise",
            style = MaterialTheme.typography.headlineSmall)

        val mostRecentWorkout = recordsViewModel.mostRecentWorkout
        if (mostRecentWorkout != null) {
            WorkoutResultsTable(listOf(mostRecentWorkout.value!!))
        } else {
            WorkoutResultsTable(emptyList())
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartExerciseCard(onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .height(200.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.situp),
                contentDescription = "sit up image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
            )
            // Translucent overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)) // Black color with 30% opacity
            )
            Text(
                text = "Start Training",
                style = MaterialTheme.typography.labelLarge.copy(color = Color.White)
            )
        }
    }
}