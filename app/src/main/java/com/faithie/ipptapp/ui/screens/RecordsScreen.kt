package com.faithie.ipptapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.faithie.ipptapp.viewmodel.RecordsViewModel

@Composable
fun RecordsScreen(
    navController: NavHostController,
    viewModel: RecordsViewModel
) {
    // UI for Records
    Column {
        Text(text = "Records Screen")
    }
}

//@Preview
//@Composable
//fun RecordsScreenPreview() {
//    val navController = rememberNavController()
//    RecordsScreen(navController, RecordsViewModel())
//}