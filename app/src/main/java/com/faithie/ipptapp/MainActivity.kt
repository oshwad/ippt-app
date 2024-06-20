package com.faithie.ipptapp

//import android.app.Activity
//import android.os.Bundle
//import android.util.Log
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Surface
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.tooling.preview.Preview
//import com.faithie.ipptapp.screens.HomeScreen
//import com.faithie.ipptapp.ui.theme.IPPTAppTheme

//class MainActivity : ComponentActivity() {
//    private val permissionHandler = PermissionHandler(this)
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        Log.d("Main","OnCreate called")
//        permissionHandler.getPermissions()
//        setContent {
//            HomeScreen(context = applicationContext)
//        }
//    }
//}

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberMaterial3
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.navArgument
import com.faithie.ipptapp.presentation.login.LoginScreen
import com.faithie.ipptapp.presentation.onboarding.OnboardingScreen
import com.faithie.ipptapp.presentation.home.HomeScreen
import com.faithie.ipptapp.presentation.navigation.MainNavGraph
import com.faithie.ipptapp.presentation.navigation.Screens
import com.faithie.ipptapp.ui.theme.IPPTAppTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IPPTAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainContent(viewModel.navController)
                }
            }
        }
    }
}

@Composable
fun MainContent(navController: NavHostController) {
    val context = LocalContext.current
    val navHostController = rememberNavController()
    val actions = remember(navHostController) { MainActions(navHostController) }

    MainNavGraph(navController = navHostController, actions = actions)
}