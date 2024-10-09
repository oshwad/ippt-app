package com.faithie.ipptapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.faithie.ipptapp.R
import com.faithie.ipptapp.ui.theme.MyAppTheme

@Composable
fun HomeScreen(navController: NavHostController) {
    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    )
    {
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Hello, {name}",
            style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(25.dp))

        StartExerciseCard(
            onClick = {
                navController.navigate(Screens.Exercise.route)
            }
        )

        Text(text = "Last Exercise Statistics",
            style = MaterialTheme.typography.headlineSmall)

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(25.dp),
            border = BorderStroke(2.dp, Color.Black)
        ){
            Box(modifier = Modifier
                .height(200.dp)
                .fillMaxSize(), contentAlignment = Alignment.Center){
                Text(text = "to do", style = MaterialTheme.typography.bodyMedium)
            }
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
            .padding(25.dp),
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

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MyAppTheme {
        HomeScreen(rememberNavController())
    }
}