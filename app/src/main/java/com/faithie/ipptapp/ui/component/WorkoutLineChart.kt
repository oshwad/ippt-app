package com.faithie.ipptapp.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.extensions.formatToSinglePrecision
import co.yml.charts.common.model.Point
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
import com.faithie.ipptapp.model.entity.WorkoutResult
import java.time.format.DateTimeFormatter

@Composable
fun WorkoutLineChart(workoutData: List<WorkoutResult>) {

    val sortedData = workoutData.reversed()
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
        .labelAndAxisLinePadding(15.dp)
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

    LineChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        lineChartData = lineChartData
    )
}