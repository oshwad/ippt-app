package com.faithie.ipptapp.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faithie.ipptapp.model.entity.WorkoutResult
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun WorkoutResultsTable(
    workoutResults: List<WorkoutResult>
) {
    // DateTime formatter
    val dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")

    Column(
        modifier = Modifier
            .fillMaxWidth()
//            .padding(16.dp)
    ) {
        // Table Header
        Row(
            modifier = Modifier
                .border(BorderStroke(1.dp, Color.Black))
                .fillMaxWidth()
                .height(30.dp),
        ) {
            Text(
                text = "Date & Time",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            )
            VerticalDivider(
                modifier = Modifier.width(1.dp),
                color = Color.Black
            )
            Text(
                text = "Push Ups",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            )
            VerticalDivider(
                modifier = Modifier.width(1.dp),
                color = Color.Black
            )
            Text(
                text = "Sit Ups",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            )
        }

        // Table Data
        workoutResults.forEach { result ->
            Row(
                modifier = Modifier
                    .border(BorderStroke(1.dp, Color.Black))
                    .fillMaxWidth()
                    .height(40.dp)
            ) {
                Text(
                    text = result.date.format(dateTimeFormatter),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f).padding(4.dp)
                )
                VerticalDivider(
                    modifier = Modifier.width(1.dp),
                    color = Color.Black
                )
                Text(
                    text = result.pushUpReps.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f).padding(4.dp)
                )
                VerticalDivider(
                    modifier = Modifier.width(1.dp),
                    color = Color.Black
                )
                Text(
                    text = result.sitUpReps.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f).padding(4.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WorkoutResultsTablePreview() {
    // Create dummy workout results
    val dummyWorkoutResults = listOf(
        WorkoutResult(date = LocalDateTime.now(), pushUpReps = 15, sitUpReps = 20),
        WorkoutResult(date = LocalDateTime.now(), pushUpReps = 10, sitUpReps = 25),
        WorkoutResult(date = LocalDateTime.now(), pushUpReps = 20, sitUpReps = 30)
    )

    // Call the WorkoutResultsTable composable with dummy data
    WorkoutResultsTable(workoutResults = dummyWorkoutResults)
}