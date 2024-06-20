package com.faithie.ipptapp.feature_camera

import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun CameraPreview(
    controller: LifecycleCameraController,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView: PreviewView = remember {
        PreviewView(context)
    }
    controller.bindToLifecycle(lifecycleOwner)
    previewView.controller = controller
    
    AndroidView(factory = {previewView})
}