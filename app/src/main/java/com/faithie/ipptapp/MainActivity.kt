package com.faithie.ipptapp

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.faithie.ipptapp.navigation.NavGraph
import com.faithie.ipptapp.posedetector.training.PoseAssetReader
import com.faithie.ipptapp.ui.screens.Screens
import com.faithie.ipptapp.ui.theme.MyAppTheme
import com.faithie.ipptapp.utils.BottomNavBar
import com.faithie.ipptapp.utils.PermissionHandler
import com.faithie.ipptapp.viewmodel.ExerciseViewModel
import com.faithie.ipptapp.viewmodel.ExerciseViewModelFactory
import com.faithie.ipptapp.viewmodel.PoseTrainingViewModel
import com.faithie.ipptapp.viewmodel.PoseTrainingViewModelFactory
import com.faithie.ipptapp.viewmodel.RecordsViewModel
import com.faithie.ipptapp.viewmodel.RecordsViewModelFactory

class MainActivity : ComponentActivity() {
    private val permissionHandler = PermissionHandler(this)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        permissionHandler.initPermissions()

        // delete or create and populate poses.csv file
        val poseAssetReader = PoseAssetReader(this)
//        poseAssetReader.deletePosesCsvFile()
//        poseAssetReader.readPoseAssets(this)

        val exerciseViewModel: ExerciseViewModel by viewModels { ExerciseViewModelFactory(application) }
        val poseTrainingViewModel: PoseTrainingViewModel by viewModels { PoseTrainingViewModelFactory(application) }
        val recordsViewModel: RecordsViewModel by viewModels { RecordsViewModelFactory(application) }

        setContent {
            MyAppTheme {
                MyApp(
                    exerciseViewModel,
                    poseTrainingViewModel,
                    recordsViewModel,
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MyApp(
    exerciseViewModel: ExerciseViewModel,
    poseTrainingViewModel: PoseTrainingViewModel,
    recordsViewModel: RecordsViewModel,
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute in listOf(Screens.Home.route, Screens.Records.route, Screens.Account.route)) {
                BottomNavBar(navController = navController)
            }
        }
    ) {
        NavGraph(
            navController = navController,
            exerciseViewModel = exerciseViewModel,
            poseTrainingViewModel = poseTrainingViewModel,
            recordsViewModel = recordsViewModel
        )
    }
}