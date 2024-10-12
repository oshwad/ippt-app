package com.faithie.ipptapp.ui.component

import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDocked(
    initialValue: String?
): LocalDate? {
    var showDatePicker by remember { mutableStateOf(false) }

    val todayMillis = remember {
        Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis() // Set to current time
        }.timeInMillis
    }

    val datePickerState = rememberDatePickerState(
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis >= todayMillis // Only allow dates from today onwards
            }
        }
    )

    val selectedDateMillis: Long? = datePickerState.selectedDateMillis

//    val selectedDate = selectedDateMillis?.let {
//        convertMillisToDateString(it)
//    } ?: ""

    var selectedDate by remember { mutableStateOf<String?>(initialValue) }

    Box(
        modifier = Modifier
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = selectedDate ?: "",
            textStyle = MaterialTheme.typography.bodyMedium,
            onValueChange = { },
            label = { Text(text = "Next IPPT Date", style = MaterialTheme.typography.labelMedium) },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDatePicker = !showDatePicker }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select date"
                    )
                }
            },
        )

        if (showDatePicker) {
            Popup(
                onDismissRequest = { showDatePicker = false },
                alignment = Alignment.TopStart
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = 64.dp)
                        .shadow(elevation = 4.dp)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp)
                ) {
                    DatePicker(
                        state = datePickerState,
                        modifier = Modifier,
                        showModeToggle = false,
                        headline = {
                            Text(
                                modifier = Modifier.padding(16.dp),
                                text = selectedDate ?: "",
                                style = MaterialTheme.typography.labelLarge
                            )
                        },
                    )
                }
            }
        }
    }

    return if (selectedDate?.isNotEmpty() == true) {
        convertDateStringToLocalDate(selectedDate!!)
    } else {
        null // Return null if no date is selected
    }
}

fun convertMillisToDateString(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val calendar = Calendar.getInstance().apply {
        timeInMillis = millis
    }
    return formatter.format(calendar.time)
}

fun convertDateStringToLocalDate(dateString: String): LocalDate? {
    if(dateString.isEmpty()) {
        return null
    }

    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    return LocalDate.parse(dateString, formatter)
}