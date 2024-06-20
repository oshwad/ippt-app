package com.faithie.ipptapp.screens

import android.content.Context
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.faithie.ipptapp.feature_posedetector.PoseDetectionAnalyser
import com.faithie.ipptapp.feature_camera.CameraPreview
import com.faithie.ipptapp.ui.theme.IPPTAppTheme
import java.util.concurrent.Executors

@Composable
fun HomeScreen (
    context: Context
) {
    var poseDisplay by remember {
        mutableStateOf<String>("")
    }
    var analyser by remember {
        mutableStateOf<PoseDetectionAnalyser>(
            PoseDetectionAnalyser(
            onPoseDetect = {
                Log.d("HomeScreen", "$it")
                poseDisplay = "${it[0].position3D}"
            },
            context
        )
        )
    }
    var executor by remember{
        mutableStateOf(Executors.newSingleThreadExecutor())
    }
    var controller by remember{ mutableStateOf(LifecycleCameraController(context).apply{
        setEnabledUseCases(
            CameraController.IMAGE_CAPTURE or
            CameraController.VIDEO_CAPTURE or
            CameraController.IMAGE_ANALYSIS
        )
        setImageAnalysisAnalyzer(
            executor,
            analyser
        )
        cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
    })}
    IPPTAppTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            CameraPreview(controller = controller)
            Text(text = poseDisplay,
                style = TextStyle(
                    color = Color.White
                )
            )
        }
    }
}
