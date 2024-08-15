package com.faithie.ipptapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun HomeScreen(navController: NavHostController) {
    // UI for Home
    Column {
        Text(text = "Home")
        Button(onClick = { navController.navigate("exercise") }) {
            Text("Go to Exercise")
        }
        Button(onClick = { navController.navigate("records") }) {
            Text("View Records")
        }
        Button(onClick = { navController.navigate("account") }) {
            Text("Account Management")
        }
    }
}