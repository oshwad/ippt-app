package com.faithie.ipptapp

import androidx.navigation.NavHostController
import com.faithie.ipptapp.presentation.navigation.Screens

class MainActions(navController: NavHostController) {

    val navigateToLogin: () -> Unit = {
        navController.navigate(Screens.Login.route)
    }

    val navigateToOnboarding: () -> Unit = {
        navController.navigate(Screens.Onboarding.route)
    }

    val navigateToHome: () -> Unit = {
        navController.navigate(Screens.Home.route)
    }
}
