package com.faithie.ipptapp.ui.component

import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlipCameraAndroid
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun CameraPreview (
    controller: LifecycleCameraController,
    modifier: Modifier = Modifier,
    onCameraChanges: (Int, Int) -> Unit
) {
    val TAG = "CameraPreview"
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView: PreviewView = remember { PreviewView(context) }

    controller.bindToLifecycle(lifecycleOwner)
    previewView.controller = controller

    // States to hold the width, height, and flipped status
    var previewWidth by remember { mutableStateOf(0) }
    var previewHeight by remember { mutableStateOf(0) }
    var isFrontFacingCam by remember { mutableStateOf(true) }

    // Check if the camera is front-facing (flipped)
    isFrontFacingCam = controller.cameraSelector == CameraSelector.DEFAULT_FRONT_CAMERA

    fun flipCamera() {
        controller.cameraSelector = if (isFrontFacingCam) {
            CameraSelector.DEFAULT_BACK_CAMERA
        } else {
            CameraSelector.DEFAULT_FRONT_CAMERA
        }
        // Update the isFlipped state based on the selected camera
        isFrontFacingCam = controller.cameraSelector == CameraSelector.DEFAULT_FRONT_CAMERA
    }

    Box(
        modifier = modifier.onSizeChanged { size ->
            previewWidth = size.width
            previewHeight = size.height
            onCameraChanges(previewWidth, previewHeight)
        }
    ) {
        AndroidView(
            factory = { previewView },
            modifier = modifier
        )
        IconButton(
            onClick = {
                flipCamera()
//                onCameraChanges(previewWidth, previewHeight)
                Log.d(TAG, "is front facing cam: $isFrontFacingCam")
            },
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(
                imageVector = Icons.Default.FlipCameraAndroid,
                contentDescription = "Flip Camera",
                tint = Color.White // Set the color of the icon
            )
        }
    }
}