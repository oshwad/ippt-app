package com.faithie.ipptapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.faithie.ipptapp.navigation.NavGraph
import com.faithie.ipptapp.ui.screens.Screen
import com.faithie.ipptapp.ui.theme.MyAppTheme
import com.faithie.ipptapp.utils.BottomNavBar
import com.faithie.ipptapp.utils.PermissionHandler
import com.faithie.ipptapp.viewmodel.ExerciseViewModel
import com.faithie.ipptapp.viewmodel.ExerciseViewModelFactory

class MainActivity : ComponentActivity() {
    private val permissionHandler = PermissionHandler(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        permissionHandler.initPermissions()

        val exerciseViewModel: ExerciseViewModel by viewModels { ExerciseViewModelFactory(application) }

        setContent {
            MyAppTheme {
                MyApp(exerciseViewModel)
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MyApp(exerciseViewModel: ExerciseViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute in listOf(Screen.Home.route, Screen.Records.route, Screen.Account.route)) {
                BottomNavBar(navController = navController)
            }
        }
    ) {
        NavGraph(navController = navController, exerciseViewModel = exerciseViewModel)
    }
}