package com.faithie.ipptapp.ui.screens

import android.app.Application
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import co.yml.charts.axis.AxisData
import co.yml.charts.common.components.Legends
import co.yml.charts.common.extensions.formatToSinglePrecision
import co.yml.charts.common.model.LegendLabel
import co.yml.charts.common.model.LegendsConfig
import co.yml.charts.common.model.Point
import co.yml.charts.ui.combinedchart.CombinedChart
import co.yml.charts.ui.combinedchart.model.CombinedChartData
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import com.faithie.ipptapp.ui.component.Legend
import com.faithie.ipptapp.ui.component.WorkoutLineChart
import com.faithie.ipptapp.ui.component.WorkoutResultsTable
import com.faithie.ipptapp.viewmodel.RecordsViewModel
import java.time.format.DateTimeFormatter

@Composable
fun RecordsScreen(
    navController: NavHostController,
    viewModel: RecordsViewModel
) {
    val TAG = "RecordsScreen"
    val workoutData = viewModel.allResults.observeAsState(initial = emptyList())

    LaunchedEffect(Unit) {
        viewModel.fetchWorkoutResults() // Triggers fetching whenever the screen is visible
    }

    LazyColumn(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
        item {
            Text(text = "Workout History", style = MaterialTheme.typography.headlineMedium)
        }
        item {
            WorkoutLineChart(workoutData.value)
        }
        item {
            Row {
                Legend(Color.Blue, "Push Ups")
                Legend(Color.Magenta, "Sit Ups")
            }
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            WorkoutResultsTable(workoutResults = workoutData.value)
        }
    }
}