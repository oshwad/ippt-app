package com.faithie.ipptapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.faithie.ipptapp.viewmodel.RecordsViewModel

@Composable
fun RecordsScreen(
    navController: NavHostController,
    viewModel: RecordsViewModel
) {
    val workoutData = viewModel.allResults.observeAsState(initial = emptyList())

    // Continue with the same chart setup logic
    val sortedData = workoutData.value.reversed()

//    val entriesPushups = sortedData.mapIndexed { index, result ->
//        Entry(index.toFloat(), result.pushUpReps.toFloat())
//    }
//    val entriesSitups = sortedData.mapIndexed { index, result ->
//        Entry(index.toFloat(), result.sitUpReps.toFloat())
//    }
//
//    val pushupDataSet = LineDataSet(entriesPushups, "Push-ups")
//    val situpDataSet = LineDataSet(entriesSitups, "Sit-ups")
//
//    pushupDataSet.color = Color.RED
//    situpDataSet.color = Color.BLUE
//
//    val lineData = LineData(pushupDataSet, situpDataSet)
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//    ) {
//        LineChart(lineData)
//        Spacer(modifier = Modifier.height(16.dp))
//    }
}

//@Preview
//@Composable
//fun RecordsScreenPreview() {
//    val navController = rememberNavController()
//    RecordsScreen(navController, RecordsViewModel())
//}