package com.faithie.ipptapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun SignUpScreen(navController: NavHostController) {
    // UI for Sign Up
    Column {
        Text(text = "Sign Up")
        Button(onClick = { navController.navigate("login") }) {
            Text("Sign Up")
        }
    }
}