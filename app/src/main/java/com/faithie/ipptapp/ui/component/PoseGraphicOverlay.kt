package com.faithie.ipptapp.ui.component

import android.content.res.Configuration
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalConfiguration
import com.google.mlkit.vision.pose.PoseLandmark

@Composable
fun PoseGraphicOverlay(
    controller: LifecycleCameraController,
    posePositions: List<PoseLandmark>,
    modifier: Modifier = Modifier
) {
    val TAG = "PoseGraphicOverlay"
    val STROKE_WIDTH = 10f
    val DOT_RADIUS = 7f

    var isFrontCamera by remember { mutableStateOf(true) }
    var flipX = -1

    val configuration = LocalConfiguration.current
    var isLandscape by remember { mutableStateOf(false) }
    var offsetX = 600
    var offsetY = 500

    val leftColor = Color.Green
    val rightColor = Color.Yellow
    val whiteColor = Color.White

    fun drawLineWithCheck(drawScope: DrawScope, startPoseLandmark: PoseLandmark?, endPoseLandmark: PoseLandmark?, color: Color) {
        if (startPoseLandmark != null && endPoseLandmark != null) {
            val startPoint = startPoseLandmark.position.let { Offset(it.x * flipX + offsetX, it.y + offsetY) }
            val endPoint = endPoseLandmark.position.let { Offset(it.x * flipX + offsetX, it.y + offsetY) }

            drawScope.drawLine(color, startPoint, endPoint, STROKE_WIDTH)
        }
    }

    isLandscape = when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> true
        Configuration.ORIENTATION_PORTRAIT -> false
        else -> true
    }
    offsetX = if (isLandscape) 900 else 600
    offsetY = if (isLandscape) 100 else 500
//    Log.d(TAG, "is landscape: $isLandscape")
//    Log.d(TAG, "offsetX: $offsetX, offsetY: $offsetY")

    LaunchedEffect(controller.cameraSelector) {
        isFrontCamera = when (controller.cameraSelector) {
            CameraSelector.DEFAULT_FRONT_CAMERA -> true
            CameraSelector.DEFAULT_BACK_CAMERA -> false
            else -> true
        }
        flipX = if (isFrontCamera) -1 else 1

        Log.d(TAG, "is front camera: $isFrontCamera")
        Log.d(TAG, "flipX == $flipX")
    }

    Box(modifier = modifier
        .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize().align(Alignment.Center)) {
            posePositions.forEach { landmark ->
                val point = Offset(landmark.position.x * flipX + offsetX, landmark.position.y + offsetY)

                drawCircle(
                    color = Color.White,
                    radius = DOT_RADIUS,
                    center = point
                )
            }

            val nose = posePositions.find { it.landmarkType == PoseLandmark.NOSE }
            val leftEyeInner = posePositions.find { it.landmarkType == PoseLandmark.LEFT_EYE_INNER }
            val leftEye = posePositions.find { it.landmarkType == PoseLandmark.LEFT_EYE }
            val leftEyeOuter = posePositions.find { it.landmarkType == PoseLandmark.LEFT_EYE_OUTER }
            val rightEyeInner = posePositions.find { it.landmarkType == PoseLandmark.RIGHT_EYE_INNER }
            val rightEye = posePositions.find { it.landmarkType == PoseLandmark.RIGHT_EYE }
            val rightEyeOuter = posePositions.find { it.landmarkType == PoseLandmark.RIGHT_EYE_OUTER }
            val leftEar = posePositions.find { it.landmarkType == PoseLandmark.LEFT_EAR }
            val rightEar = posePositions.find { it.landmarkType == PoseLandmark.RIGHT_EAR }
            val leftMouth = posePositions.find { it.landmarkType == PoseLandmark.LEFT_MOUTH }
            val rightMouth = posePositions.find { it.landmarkType == PoseLandmark.RIGHT_MOUTH }

            val leftShoulder = posePositions.find { it.landmarkType == PoseLandmark.LEFT_SHOULDER }
            val rightShoulder = posePositions.find { it.landmarkType == PoseLandmark.RIGHT_SHOULDER }
            val leftElbow = posePositions.find { it.landmarkType == PoseLandmark.LEFT_ELBOW }
            val rightElbow = posePositions.find { it.landmarkType == PoseLandmark.RIGHT_ELBOW }
            val leftWrist = posePositions.find { it.landmarkType == PoseLandmark.LEFT_WRIST }
            val rightWrist = posePositions.find { it.landmarkType == PoseLandmark.RIGHT_WRIST }
            val leftHip = posePositions.find { it.landmarkType == PoseLandmark.LEFT_HIP }
            val rightHip = posePositions.find { it.landmarkType == PoseLandmark.RIGHT_HIP }
            val leftKnee = posePositions.find { it.landmarkType == PoseLandmark.LEFT_KNEE }
            val rightKnee = posePositions.find { it.landmarkType == PoseLandmark.RIGHT_KNEE }
            val leftAnkle = posePositions.find { it.landmarkType == PoseLandmark.LEFT_ANKLE }
            val rightAnkle = posePositions.find { it.landmarkType == PoseLandmark.RIGHT_ANKLE }

            val leftPinky = posePositions.find { it.landmarkType == PoseLandmark.LEFT_PINKY }
            val rightPinky = posePositions.find { it.landmarkType == PoseLandmark.RIGHT_PINKY }
            val leftIndex = posePositions.find { it.landmarkType == PoseLandmark.LEFT_INDEX }
            val rightIndex = posePositions.find { it.landmarkType == PoseLandmark.RIGHT_INDEX }
            val leftThumb = posePositions.find { it.landmarkType == PoseLandmark.LEFT_THUMB }
            val rightThumb = posePositions.find { it.landmarkType == PoseLandmark.RIGHT_THUMB }
            val leftHeel = posePositions.find { it.landmarkType == PoseLandmark.LEFT_HEEL }
            val rightHeel = posePositions.find { it.landmarkType == PoseLandmark.RIGHT_HEEL }
            val leftFootIndex = posePositions.find { it.landmarkType == PoseLandmark.LEFT_FOOT_INDEX }
            val rightFootIndex = posePositions.find { it.landmarkType == PoseLandmark.RIGHT_FOOT_INDEX }

            // Face
            drawLineWithCheck(this, nose, leftEyeInner, whiteColor)
            drawLineWithCheck(this, leftEyeInner, leftEye, whiteColor)
            drawLineWithCheck(this, leftEye, leftEyeOuter, whiteColor)
            drawLineWithCheck(this, leftEyeOuter, leftEar, whiteColor)
            drawLineWithCheck(this, nose, rightEyeInner, whiteColor)
            drawLineWithCheck(this, rightEyeInner, rightEye, whiteColor)
            drawLineWithCheck(this, rightEye, rightEyeOuter, whiteColor)
            drawLineWithCheck(this, rightEyeOuter, rightEar, whiteColor)
            drawLineWithCheck(this, leftMouth, rightMouth, whiteColor)

            drawLineWithCheck(this, leftShoulder, rightShoulder, whiteColor)
            drawLineWithCheck(this, leftHip, rightHip, whiteColor)

            // Left body
            drawLineWithCheck(this, leftShoulder, leftElbow, leftColor)
            drawLineWithCheck(this, leftElbow, leftWrist, leftColor)
            drawLineWithCheck(this, leftShoulder, leftHip, leftColor)
            drawLineWithCheck(this, leftHip, leftKnee, leftColor)
            drawLineWithCheck(this, leftKnee, leftAnkle, leftColor)
            drawLineWithCheck(this, leftWrist, leftThumb, leftColor)
            drawLineWithCheck(this, leftWrist, leftPinky, leftColor)
            drawLineWithCheck(this, leftWrist, leftIndex, leftColor)
            drawLineWithCheck(this, leftIndex, leftPinky, leftColor)
            drawLineWithCheck(this, leftAnkle, leftHeel, leftColor)
            drawLineWithCheck(this, leftHeel, leftFootIndex, leftColor)

            // Right body
            drawLineWithCheck(this, rightShoulder, rightElbow, rightColor)
            drawLineWithCheck(this, rightElbow, rightWrist, rightColor)
            drawLineWithCheck(this, rightShoulder, rightHip, rightColor)
            drawLineWithCheck(this, rightHip, rightKnee, rightColor)
            drawLineWithCheck(this, rightKnee, rightAnkle, rightColor)
            drawLineWithCheck(this, rightWrist, rightThumb, rightColor)
            drawLineWithCheck(this, rightWrist, rightPinky, rightColor)
            drawLineWithCheck(this, rightWrist, rightIndex, rightColor)
            drawLineWithCheck(this, rightIndex, rightPinky, rightColor)
            drawLineWithCheck(this, rightAnkle, rightHeel, rightColor)
            drawLineWithCheck(this, rightHeel, rightFootIndex, rightColor)

        }
    }
}