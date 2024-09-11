package com.faithie.ipptapp.ui.screens

import android.annotation.SuppressLint
import androidx.camera.core.CameraSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.ProcessCameraProvider.getInstance
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.faithie.ipptapp.posedetector.processing.GraphicOverlay
import com.faithie.ipptapp.posedetector.processing.VisionImageProcessor
import com.faithie.ipptapp.ui.component.CameraPreviewView
import com.faithie.ipptapp.ui.component.CameraPreviewWithGraphicOverlay
import com.faithie.ipptapp.viewmodel.ExerciseViewModel
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Composable
fun ExerciseScreen(
    navController: NavHostController,
    viewModel: ExerciseViewModel,
) {

    CameraPreviewWithGraphicOverlay(controller = viewModel.controller.value,
        posePositions = viewModel.poseLandmarksLiveData.value)
    Text(
        text = "Pose landmarks: ${
            if (!viewModel.poseLandmarksLive.value.isNullOrEmpty())
                viewModel.poseLandmarksLive.value[0].position3D
            else
                ""
        }"
    )
}

//@Preview
//@Composable
//fun ExerciseScreenPreview() {
//    ExerciseScreen(rememberNavController(), ExerciseViewModel())
//}