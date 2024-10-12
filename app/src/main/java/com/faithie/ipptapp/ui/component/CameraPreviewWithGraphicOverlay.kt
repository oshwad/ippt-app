package com.faithie.ipptapp.ui.component

import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.google.mlkit.vision.common.PointF3D
import com.google.mlkit.vision.pose.PoseLandmark

@Composable
fun CameraPreviewWithGraphicOverlay(
    controller: LifecycleCameraController,
    posePositions: List<PoseLandmark>,
    imageDims: State<Pair<Int, Int>>,
    modifier: Modifier = Modifier
) {
    val TAG = "CameraPreviewWithGraphicOverlay"
    var viewWidth by remember { mutableIntStateOf(0) }
    var viewHeight by remember { mutableIntStateOf(0) }
    var viewFlipped by remember { mutableStateOf(true) }
    var flipX = -1
    var imageWidth by remember { mutableIntStateOf(imageDims.value.first) }
    imageWidth = 640
    var imageHeight by remember { mutableIntStateOf(imageDims.value.second) }
    imageHeight = 480
    Log.d(TAG, "imageWidth: $imageWidth, imageHeight: $imageHeight")

    LaunchedEffect(controller.cameraSelector) {
        // Check the camera selector and update the flip status
        viewFlipped = when (controller.cameraSelector) {
            CameraSelector.DEFAULT_FRONT_CAMERA -> true // Front camera is flipped
            CameraSelector.DEFAULT_BACK_CAMERA -> false // Back camera is not flipped
            else -> true
        }

        Log.d(TAG, "Camera flipped: $viewFlipped")
    }

    var scaleFactor by remember { mutableFloatStateOf(0f) }
    var postScaleWidthOffset by remember { mutableFloatStateOf(0f) }
    var postScaleHeightOffset by remember { mutableFloatStateOf(0f) }

//    var viewAspectRatio = viewWidth/viewHeight
//    var imageAspectRatio = imageWidth/imageHeight
//    if (viewAspectRatio > imageAspectRatio) {
//        scaleFactor = (viewWidth/imageWidth).toFloat()
//        postScaleHeightOffset = ((viewWidth / imageAspectRatio - viewHeight) / 2).toFloat()
//    } else {
//        scaleFactor = (viewHeight/imageHeight).toFloat()
//        postScaleWidthOffset = ((viewHeight * imageAspectRatio - viewWidth) / 2).toFloat()
//    }

    fun updateScaleFactors() {
        if (viewWidth > 0 && viewHeight > 0) {
            val viewAspectRatio = viewWidth.toFloat() / viewHeight
            val imageAspectRatio = imageWidth.toFloat() / imageHeight
            if (viewAspectRatio > imageAspectRatio) {
                scaleFactor = (viewWidth.toFloat() / imageWidth).toFloat()
                postScaleHeightOffset = ((viewWidth / imageAspectRatio - viewHeight) / 2).toFloat()
            } else {
                scaleFactor = (viewHeight.toFloat() / imageHeight).toFloat()
                postScaleWidthOffset = ((viewHeight * imageAspectRatio - viewWidth) / 2).toFloat()
            }
        }
    }

    val leftColor = Color.Green
    val rightColor = Color.Yellow
    val whiteColor = Color.White

    fun scale(imagePixel: Float): Float {
        return (imagePixel * scaleFactor)
    }

    fun translateX(x: Float): Float {
        if (viewFlipped) {
            return viewWidth - (scale(x) - postScaleWidthOffset)
        } else {
            return scale(x) - postScaleWidthOffset
        }
    }

    fun translateY(y: Float): Float {
        return scale(y) - postScaleHeightOffset
    }

    fun drawPoint(drawScope: DrawScope, landmark: PoseLandmark) {
        val offsetPoint = Offset(
            x = translateX(landmark.position.x) * flipX,
            y = translateY(landmark.position.y)
        )
        drawScope.drawCircle(
            color = Color.White,
            radius = 8.0f,
            center = offsetPoint
        )
    }

    fun drawLine(drawScope: DrawScope, startLandmark: PoseLandmark, endLandmark: PoseLandmark, color: Color) {
        val offsetStart = Offset(
            x = translateX(startLandmark.position.x) * flipX,
            y = translateY(startLandmark.position.y)
        )
        val offsetEnd = Offset(
            x = translateX(endLandmark.position.x) * flipX,
            y = translateY(endLandmark.position.y)
        )
        drawScope.drawLine(
            color = color,
            start = offsetStart,
            end = offsetEnd,
            strokeWidth = 10.0f
        )
    }

    Box(modifier = modifier
        .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CameraPreview(
            controller,
            Modifier.fillMaxSize(),
            onCameraChanges = { width, height ->
                viewWidth = width
                viewHeight = height
                Log.d(TAG, "viewWidth: $viewWidth, viewHeight: $viewHeight")
                updateScaleFactors()
            }
        )

        // Custom overlay for drawing the pose
//        Canvas(modifier = Modifier.fillMaxSize().align(Alignment.Center)) {
//
//            posePositions.forEach { landmark ->
//                if (viewFlipped) {
//                    flipX = -1
//                }
//                else {
//                    flipX = 1
//                }
////                val point = Offset(landmark.position.x * flipX + 700, landmark.position.y + 500)
////                drawCircle(
////                    color = Color.White,
////                    radius = 8f,
////                    center = point
////                )
//                drawPoint(this, landmark)
//            }
//        }
        PoseGraphicOverlay(controller = controller, posePositions = posePositions)
    }
}