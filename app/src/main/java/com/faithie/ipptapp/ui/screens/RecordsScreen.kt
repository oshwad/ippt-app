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

    val sortedData = workoutData.value.reversed()
    val pushUpReps = sortedData.map { it.pushUpReps.toFloat() }
    val sitUpReps = sortedData.map { it.sitUpReps.toFloat() }
    val formatter = DateTimeFormatter.ofPattern("dd MMM")
    val dates = sortedData.map { it.date.format(formatter) }

    val pushUpPoints: List<Point> = pushUpReps.mapIndexed { index, reps -> Point(index.toFloat(), reps) }
    val sitUpPoints: List<Point> = sitUpReps.mapIndexed { index, reps -> Point(index.toFloat(), reps) }

    val xAxisData = AxisData.Builder()
        .axisStepSize(100.dp)
        .steps(pushUpPoints.size - 1)
        .labelData { i -> dates[i] }
        .labelAndAxisLinePadding(15.dp)
        .build()

    val maxReps = maxOf(pushUpReps.maxOrNull() ?: 0f, sitUpReps.maxOrNull() ?: 0f) // Get max value
    val steps: Int = 1
    val yAxisData = AxisData.Builder()
        .steps(steps)
        .labelAndAxisLinePadding(20.dp)
        .labelData { i ->
            val yScale = maxReps / steps.toFloat()
            (i * yScale).formatToSinglePrecision()
        }.build()

    val lineChartData = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = pushUpPoints,
                    LineStyle(color = Color.Blue),
                    IntersectionPoint(),
                    SelectionHighlightPoint(),
                    ShadowUnderLine(),
                    SelectionHighlightPopUp(
                        popUpLabel = { xValue, yValue ->
                            "Push Ups: ${yValue.toInt()}"
                        }
                    )
                ),
                Line( // For Sit Ups
                    dataPoints = sitUpPoints,
                    LineStyle(color = Color.Magenta), // Different color for Sit Ups
                    IntersectionPoint(),
                    SelectionHighlightPoint(),
                    ShadowUnderLine(),
                    SelectionHighlightPopUp(
                        popUpLabel = { xValue, yValue ->
                            "Sit Ups: ${yValue.toInt()}"
                        }
                    )
                )
            ),
        ),
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        gridLines = GridLines(),
        backgroundColor = Color.Transparent
    )

    LazyColumn(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(text = "Workout History", style = MaterialTheme.typography.headlineMedium)
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            LineChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                lineChartData = lineChartData
            )
        }
        item {
            val pushUpLegend: LegendLabel = LegendLabel(Color.Blue, "Push Ups")
            val sitUpLegend: LegendLabel = LegendLabel(Color.Magenta, "Sit Ups")
            val legendLabel = listOf(pushUpLegend, sitUpLegend)
//            Legends(
//                legendsConfig = LegendsConfig(
//                    legendLabelList = legendLabel
//                )
//            )
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