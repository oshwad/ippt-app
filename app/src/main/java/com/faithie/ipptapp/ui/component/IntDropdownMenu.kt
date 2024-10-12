package com.faithie.ipptapp.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import co.yml.charts.common.extensions.isNotNull

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IntDropdownMenu(
    textLabel: String,
    options: List<Int>,
    initialValue: Int?,
    modifier: Modifier
): Int? {
    var expanded by remember { mutableStateOf(false) }
    var selectedValue by remember { mutableStateOf<Int?>(initialValue) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            modifier = modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable),
            readOnly = true,
            value = selectedValue?.toString() ?: "",
            onValueChange = {},
            label = { Text(text = textLabel, style = MaterialTheme.typography.labelMedium) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            textStyle = MaterialTheme.typography.bodyMedium,
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(
                        option.toString(),
                        style = MaterialTheme.typography.bodyMedium
                    ) },
                    onClick = {
                        selectedValue = option
                        expanded = false
                    }
                )
            }
        }
    }

    return selectedValue
}