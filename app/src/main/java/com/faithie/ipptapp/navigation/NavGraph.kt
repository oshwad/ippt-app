package com.faithie.ipptapp.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.faithie.ipptapp.ui.screens.*
import com.faithie.ipptapp.viewmodel.ExerciseViewModel
import com.faithie.ipptapp.viewmodel.ExerciseViewModelFactory

@Composable
fun NavGraph(navController: NavHostController, exerciseViewModel: ExerciseViewModel) {

    Log.d("NavGraph", "Navigating to start destination: ${Screen.Login.route}")
    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(Screen.Login.route) {
            Log.d("NavGraph", "Showing Login Screen")
            LoginScreen(navController) }
        composable(Screen.SignUp.route) {
            Log.d("NavGraph", "Showing SignUp Screen")
            SignUpScreen(navController) }
        composable(Screen.Home.route) {
            Log.d("NavGraph", "Showing Home Screen")
            HomeScreen(navController) }
        composable(Screen.Exercise.route) {
            Log.d("NavGraph", "Showing Exercise Screen")
            ExerciseScreen(navController, exerciseViewModel) }
        composable(Screen.Records.route) {
            Log.d("NavGraph", "Showing Records Screen")
            RecordsScreen(navController) }
        composable(Screen.Account.route) {
            Log.d("NavGraph", "Showing Account Screen")
            AccountScreen(navController) }
    }
}