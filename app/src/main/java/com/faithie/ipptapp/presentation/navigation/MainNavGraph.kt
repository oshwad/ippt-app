package com.faithie.ipptapp.presentation.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import com.faithie.ipptapp.presentation.login.LoginScreen
import com.faithie.ipptapp.presentation.onboarding.OnboardingScreen
import com.faithie.ipptapp.presentation.home.HomeScreen
import com.faithie.ipptapp.presentation.profile.ProfileScreen
import com.faithie.ipptapp.presentation.mydata.MyDataScreen
import com.faithie.ipptapp.presentation.ipptinfo.IPPTInfoScreen
import com.faithie.ipptapp.presentation.navigation.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavGraph(navController: NavHostController) {
    NavHost(navController, startDestination = Screens.Login.route) {
        composable(Screens.Login.route) {
            LoginScreen(navController)
        }
        composable(Screens.Onboarding.route) {
            OnboardingScreen(navController)
        }
        composable(Screens.Home.route) {
            HomeScreen(navController)
        }
        composable(Screens.Profile.route) {
            ProfileScreen(navController)
        }
        composable(Screens.MyData.route) {
            MyDataScreen(navController)
        }
        composable(
            route = "${Screens.IPPTInfo.route}/{username}",
            arguments = listOf(navArgument("username") { type = NavType.StringType })
        ) { entry ->
            IPPTInfoScreen(navController, entry.arguments?.getString("username"))
        }
    }
}
