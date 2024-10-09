package com.faithie.ipptapp.ui.component

import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.layout.onSizeChanged
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark

@Composable
fun CameraPreviewWithGraphicOverlay(
    controller: LifecycleCameraController,
    posePositions: List<PoseLandmark>, // Add this parameter for pose positions
    modifier: Modifier = Modifier
) {
    var viewWidth by remember { mutableIntStateOf(0) }
    var viewHeight by remember { mutableIntStateOf(0) }
    var viewFlipped by remember { mutableStateOf(true) }
    var flipX = -1

    Box(modifier = modifier
        .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CameraPreview(
            controller,
            Modifier.fillMaxSize(),
            onSizeChanged = { width, height, isFlipped ->
                viewWidth = width
                viewHeight = height
                viewFlipped = isFlipped
            }
        )

        // Custom overlay for drawing the pose
        Canvas(modifier = Modifier.fillMaxSize().align(Alignment.Center)) {
            // Paints for left and right body parts
            val leftPaint = Paint().apply {
                color = Color.Green
                strokeWidth = 10f
                style = PaintingStyle.Stroke
            }
            val rightPaint = Paint().apply {
                color = Color.Yellow
                strokeWidth = 10f
                style = PaintingStyle.Stroke
            }
            val whitePaint = Paint().apply {
                color = Color.White
                strokeWidth = 10f
                style = PaintingStyle.Stroke
            }

            // Loop through the landmarks to draw points
            posePositions.forEach { landmark ->
                if (viewFlipped) {
                    flipX = -1
                }
                else {
                    flipX = 1
                }
                val point = Offset(landmark.position.x * flipX + 700, landmark.position.y + 500)
                drawCircle(
                    color = Color.White,
                    radius = 8f,
                    center = point
                )
            }

            // example: draw a line between left shoulder and left elbow
//            val leftShoulder = posePositions.find { it.landmarkType == PoseLandmark.LEFT_SHOULDER }
//            val leftElbow = posePositions.find { it.landmarkType == PoseLandmark.LEFT_ELBOW }
//
//            if (leftShoulder != null && leftElbow != null) {
//                val startPoint = Offset(leftShoulder.position.x, leftShoulder.position.y)
//                val endPoint = Offset(leftElbow.position.x, leftElbow.position.y)
//
//                // Draw the line between the shoulder and elbow
//                drawLine(
//                    color = Color.Green,
//                    start = startPoint,
//                    end = endPoint,
//                    strokeWidth = 10f
//                )
//            }
        }

    }
}

fun translateX(x: Float, imageWidth: Int, viewWidth: Int): Float {
    // Scale the X coordinate
    val scaleX = viewWidth.toFloat() / imageWidth
    return x * scaleX
}

fun translateY(y: Float, imageHeight: Int, viewHeight: Int): Float {
    // Scale the Y coordinate
    val scaleY = viewHeight.toFloat() / imageHeight
    return y * scaleY
}