package com.faithie.ipptapp.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.faithie.ipptapp.ui.screens.*
import com.faithie.ipptapp.viewmodel.ExerciseViewModel
import com.faithie.ipptapp.viewmodel.PoseTrainingViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    exerciseViewModel: ExerciseViewModel,
    poseTrainingViewModel: PoseTrainingViewModel) {

    Log.d("NavGraph", "Navigating to start destination: ${Screens.Home.route}")
    NavHost(navController = navController, startDestination = Screens.Home.route) {
        composable(Screens.Login.route) {
            Log.d("NavGraph", "Showing Login Screen")
            LoginScreen(navController) }
        composable(Screens.SignUp.route) {
            Log.d("NavGraph", "Showing SignUp Screen")
            SignUpScreen(navController) }
        composable(Screens.Home.route) {
            Log.d("NavGraph", "Showing Home Screen")
            HomeScreen(navController) }
        composable(Screens.Exercise.route) {
            Log.d("NavGraph", "Showing Exercise Screen")
            ExerciseScreen(navController, exerciseViewModel) }
        composable(Screens.Records.route) {
            Log.d("NavGraph", "Showing Records Screen")
            RecordsScreen(navController) }
        composable(Screens.Account.route) {
            Log.d("NavGraph", "Showing Account Screen")
            AccountScreen(navController) }
        composable(Screens.PoseTraining.route) {
            PoseTrainingScreen(navController, poseTrainingViewModel)
        }
        composable(Screens.ExerciseResults.route) {
            ExerciseResultsScreen(navController, exerciseViewModel)
        }
    }
}