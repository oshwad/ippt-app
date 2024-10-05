package com.faithie.ipptapp.ui.component

import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import com.google.mlkit.vision.pose.PoseLandmark

@Composable
fun PoseGraphicOverlay(
    controller: LifecycleCameraController,
    posePositions: List<PoseLandmark>,
    modifier: Modifier = Modifier
) {
    var viewWidth by remember { mutableIntStateOf(0) }
    var viewHeight by remember { mutableIntStateOf(0) }

    Box(modifier = modifier
        .fillMaxSize()
        .onSizeChanged { size ->
            viewWidth = size.width
            viewHeight = size.height
        }
    ) {
        Canvas(modifier = Modifier.fillMaxSize().align(Alignment.Center)) {

        }
    }
}