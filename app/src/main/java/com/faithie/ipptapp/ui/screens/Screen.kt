package com.faithie.ipptapp.ui.screens

sealed class Screen(val route: String) {
    object Account: Screen("account")
    object Exercise: Screen("exercise")
    object Home: Screen("home")
    object Login: Screen("login")
    object Records: Screen("records")
    object SignUp: Screen("signup")
}
