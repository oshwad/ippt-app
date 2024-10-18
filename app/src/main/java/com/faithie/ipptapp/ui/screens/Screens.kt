package com.faithie.ipptapp.ui.screens

sealed class Screens(val route: String) {
    object Account: Screens("account")
    object Exercise: Screens("exercise")
    object Home: Screens("home")
    object Login: Screens("login")
    object Records: Screens("records")
    object SignUp: Screens("signup")
    object PoseTraining: Screens("pose_training")
    object WorkoutResults: Screens("exercise-results")
    object UserDetailsFormScreen: Screens("user-details-form")
    object DevExercise: Screens("dev-exercise")
}
