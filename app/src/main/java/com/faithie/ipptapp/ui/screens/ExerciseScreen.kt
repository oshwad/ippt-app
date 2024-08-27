package com.faithie.ipptapp.ui.screens

import android.annotation.SuppressLint
import androidx.camera.core.CameraSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
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
import com.faithie.ipptapp.viewmodel.ExerciseViewModel
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Composable
fun ExerciseScreen(
    navController: NavHostController,
    viewModel: ExerciseViewModel
) {
//    Text(text = "Exercise Screen")
    var repCount by remember { mutableIntStateOf(0) }
    var cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()

    Box(modifier = Modifier.fillMaxSize()) {
        CameraPreview(
            onRepDetected = { detectedReps ->
                repCount += detectedReps
            },
            executor = cameraExecutor
        )

        Box(modifier = Modifier.align(Alignment.TopCenter)) {
            Text(text = "Reps: $repCount", modifier = Modifier.padding(16.dp))
        }
    }
}

@SuppressLint("RestrictedApi")
@Composable
fun CameraPreview(
    onRepDetected: (Int) -> Unit,
    executor: ExecutorService
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    AndroidView(
        factory = { it ->
            val previewView = PreviewView(it)
            val cameraProviderFuture = ProcessCameraProvider.getInstance(it)
            
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                val preview = androidx.camera.core.Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

                val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
                val options = PoseDetectorOptions.Builder()
                    .setDetectorMode(PoseDetectorOptions.STREAM_MODE)
                    .build()

                val poseDetector = PoseDetection.getClient(options)

                // Camera setup and ML Kit pose detection implementation would be here
//                val imageAnalyzer = ImageAnalysis.Builder()
//                    .build()
//                    .also {
//                        it.setAnalyzer(ContextCompat.getMainExecutor(ctx), { imageProxy ->
//                            viewModel.analyzeImage(imageProxy)
//                        })
//                    }

                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
//                        imageAnalyzer
                    )
                } catch (exc: Exception) {
                    // Handle exceptions
                }

                cameraProvider.bindToLifecycle(
                    lifecycleOwner, cameraSelector, preview
                )

            }, ContextCompat.getMainExecutor(it))

            previewView
        },
        modifier = Modifier
            .fillMaxSize()
    )
}

@Preview
@Composable
fun ExerciseScreenPreview() {
    ExerciseScreen(rememberNavController(), ExerciseViewModel())
}