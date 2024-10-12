package com.faithie.ipptapp.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Composable
fun DayCountdown(nextIpptDate: LocalDate?) {
    val countdown = remember { mutableStateOf<Long?>(null) }

    LaunchedEffect(nextIpptDate) {
        nextIpptDate?.let { date ->
            val today = LocalDate.now()
            val daysUntilIppt = ChronoUnit.DAYS.between(today, date)
            countdown.value = daysUntilIppt
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        countdown.value?.let {
            if (it >= 0) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = androidx.compose.foundation.shape.CircleShape
                        ),
                    contentAlignment = Alignment.Center // Center the text in the box
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = it.toString(),
                            style = MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.onPrimaryContainer),
                            fontSize = 30.sp
                        )
                        Text(
                            text = "Days",
                            style = MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.onPrimaryContainer),
                            fontSize = 10.sp
                        )
                    }
                }
            } else {
                Text(
                    text = "IPPT date already passed",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } ?: run {
            Text(
                text = "No IPPT date set",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
