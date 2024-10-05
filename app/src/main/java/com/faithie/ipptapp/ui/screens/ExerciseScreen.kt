package com.faithie.ipptapp.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.faithie.ipptapp.ui.component.CameraPreviewWithGraphicOverlay
import com.faithie.ipptapp.viewmodel.ExerciseViewModel

@Composable
fun ExerciseScreen(
    navController: NavHostController,
    viewModel: ExerciseViewModel,
) {

    CameraPreviewWithGraphicOverlay(controller = viewModel.controller.value,
        posePositions = viewModel.poseLandmarks.value)
    Text(
        text = "Pose landmarks: ${
            if (!viewModel.poseLandmarks.value.isNullOrEmpty())
                viewModel.poseLandmarks.value[0].position3D
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