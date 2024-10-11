package com.faithie.ipptapp.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.faithie.ipptapp.viewmodel.UserViewModel

@Composable
fun AccountScreen(
    navController: NavHostController,
    viewModel: UserViewModel
) {
    Text("Account Screen")
}