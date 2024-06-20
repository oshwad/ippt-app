package com.faithie.ipptapp.presentation.navigation

sealed class Screens(val route: String) {
    object Login : Screens("login")
    object Onboarding : Screens("onboarding")
    object Home : Screens("home")
    object Profile : Screens("profile")
    object MyData : Screens("mydata")
    object IPPTInfo : Screens("ipptinfo")
}