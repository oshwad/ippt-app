package com.faithie.ipptapp.ui.navigation

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.faithie.ipptapp.model.entity.User
import com.faithie.ipptapp.ui.screens.*
import com.faithie.ipptapp.viewmodel.ExerciseViewModel
import com.faithie.ipptapp.viewmodel.PoseTrainingViewModel
import com.faithie.ipptapp.viewmodel.RecordsViewModel
import com.faithie.ipptapp.viewmodel.UserViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(
    navController: NavHostController,
    exerciseViewModel: ExerciseViewModel,
    poseTrainingViewModel: PoseTrainingViewModel,
    recordsViewModel: RecordsViewModel,
    userViewModel: UserViewModel
) {
    var startDest = ""
    var user by remember { mutableStateOf<User?>(null) }

    LaunchedEffect(Unit) {
        user = userViewModel.getUser()
    }

    startDest = if (user == null) {
        Screens.UserDetailsFormScreen.route
    } else {
        Screens.Home.route
    }

    NavHost(navController = navController, startDestination = startDest) {
        composable(Screens.Login.route) {
            Log.d("NavGraph", "Showing Login Screen")
            LoginScreen(navController) }
        composable(Screens.SignUp.route) {
            Log.d("NavGraph", "Showing SignUp Screen")
            SignUpScreen(navController) }
        composable(Screens.Home.route) {
            Log.d("NavGraph", "Showing Home Screen")
            HomeScreen(navController, userViewModel, recordsViewModel) }
        composable(Screens.Exercise.route) {
            Log.d("NavGraph", "Showing Exercise Screen")
            ExerciseScreen(navController, exerciseViewModel) }
        composable(Screens.Records.route) {
            Log.d("NavGraph", "Showing Records Screen")
            RecordsScreen(navController, recordsViewModel) }
        composable(Screens.Account.route) {
            Log.d("NavGraph", "Showing Account Screen")
            AccountScreen(navController, userViewModel) }
        composable(Screens.PoseTraining.route) {
            PoseTrainingScreen(navController, poseTrainingViewModel)
        }
        composable(Screens.WorkoutResults.route) {
            WorkoutResultsScreen(navController, exerciseViewModel)
        }
        composable(Screens.UserDetailsFormScreen.route) {
            UserDetailsFormScreen(navController, userViewModel)
        }
    }
}