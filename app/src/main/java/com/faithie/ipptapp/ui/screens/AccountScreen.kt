package com.faithie.ipptapp.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun AccountScreen(navController: NavController) {
    Text("Account Screen")
}

@Preview
@Composable
fun AccountScreenPreview() {
    val navController = rememberNavController()
    AccountScreen(navController)
}