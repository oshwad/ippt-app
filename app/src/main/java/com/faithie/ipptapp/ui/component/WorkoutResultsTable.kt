package com.faithie.ipptapp.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faithie.ipptapp.model.entity.WorkoutResult
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun WorkoutResultsTable(
    workoutResults: List<WorkoutResult>,
    itemsPerPage: Int = 5 // Number of items per page
) {
    // DateTime formatter
    val dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")

    // Pagination state
    var currentPage by remember { mutableStateOf(0) }
    val totalPages = (workoutResults.size + itemsPerPage - 1) / itemsPerPage // Calculate total pages

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
                .height(30.dp)
                .background(color = Color.LightGray)
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

        // Table Data (Paginated)
        val startIndex = currentPage * itemsPerPage
        val endIndex = minOf(startIndex + itemsPerPage, workoutResults.size)
        val paginatedResults = workoutResults.subList(startIndex, endIndex)

        paginatedResults.forEach { result ->
            Row(
                modifier = Modifier
                    .border(BorderStroke(1.dp, Color.Black))
                    .fillMaxWidth()
                    .height(40.dp)
            ) {
                Text(
                    text = result.date.format(dateTimeFormatter),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp)
                )
                VerticalDivider(
                    modifier = Modifier.width(1.dp),
                    color = Color.Black
                )
                Text(
                    text = result.pushUpReps.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp)
                )
                VerticalDivider(
                    modifier = Modifier.width(1.dp),
                    color = Color.Black
                )
                Text(
                    text = result.sitUpReps.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp)
                )
            }
        }

        // Pagination controls
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 5.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { if (currentPage > 0) currentPage-- }, // Decrease page if not on the first page
                enabled = currentPage > 0
            ) {
                Text("Previous", style = MaterialTheme.typography.labelMedium,)
            }
            Text(
                text = "Page ${currentPage + 1} of $totalPages",
                modifier = Modifier.align(Alignment.CenterVertically),
                style = MaterialTheme.typography.bodyMedium,
            )
            Button(
                onClick = { if (currentPage < totalPages - 1) currentPage++ }, // Increase page if not on the last page
                enabled = currentPage < totalPages - 1
            ) {
                Text("Next", style = MaterialTheme.typography.labelMedium,)
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