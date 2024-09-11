package com.faithie.ipptapp.ui.component

import android.util.Log
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import com.google.mlkit.vision.pose.PoseLandmark

@Composable
fun CameraPreviewWithGraphicOverlay(
    controller: LifecycleCameraController,
    posePositions: List<PoseLandmark>, // Add this parameter for pose positions
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier
        .fillMaxSize(),
        contentAlignment = Alignment.Center) {
        CameraPreview(controller, Modifier.fillMaxSize())

        Canvas(
            modifier = Modifier
                .fillMaxSize()
//                .background(Color.White.copy(alpha = 0.8f))
                .align(Alignment.Center)
        ) {
            // Inside the Canvas, draw the camera preview overlay and boxes based on pose positions
            drawIntoCanvas { canvas ->
                if (posePositions.isNotEmpty()){
                    var nose = posePositions[0].position3D

                    val positionX = nose.x + 250f // X-coordinate of the landmark
                    val positionY = nose.y // Y-coordinate of the landmark
                    val boxSize = 50f // Size of the box (Box around eadch landmark
                    val left = positionX - boxSize / 2
                    val top = positionY - boxSize / 2
                    val right = positionX + boxSize / 2
                    val bottom = positionY + boxSize / 2

                    val paint = Paint().apply {
                        color = Color.Red
                        style = PaintingStyle.Stroke
                        strokeCap = StrokeCap.Round
                        strokeWidth = 2f
                    }

                    canvas.drawRect(left, top, right, bottom, paint)
                }else{
                    Log.d("CameraPreviewWithGraphicOverlay", "Pose positions is empty")
                }
            }
        }

    }
}