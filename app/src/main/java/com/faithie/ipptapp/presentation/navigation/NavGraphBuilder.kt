package com.faithie.ipptapp.presentation.navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.compose.rememberNavController
import com.faithie.ipptapp.presentation.navigation.Screens

@OptIn(ExperimentalMaterial3Api::class)
fun ComponentActivity.NavGraphBuilder() {
    val navController = rememberNavController()
    setContent {
        MainNavGraph(navController)
    }
}
