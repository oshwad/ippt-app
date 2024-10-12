package com.faithie.ipptapp.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Legend(color: Color, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(4.dp)
    ) {
        // Box for the colored square
        Box(
            modifier = Modifier
                .size(16.dp) // Size of the color box
                .background(color) // Box color
        )

        Spacer(modifier = Modifier.width(8.dp)) // Space between box and text

        // Label text next to the color box
        Text(text = label, style = MaterialTheme.typography.labelSmall)
    }
}