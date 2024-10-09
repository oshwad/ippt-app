package com.faithie.ipptapp.ui.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.faithie.ipptapp.R

// typography
val HelveticaNeueExtBold = FontFamily(
    Font(R.font.helvetica_neue_73_bold_extended)
)
val Helvetica = FontFamily(
    Font(R.font.helvetica)
)
val CustomTypography = Typography(
    headlineLarge = TextStyle(
        fontFamily = HelveticaNeueExtBold,
        fontSize = 36.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = HelveticaNeueExtBold,
        fontSize = 30.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = HelveticaNeueExtBold,
        fontSize = 24.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = Helvetica,
        fontSize = 20.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = Helvetica,
        fontSize = 16.sp
    ),
    bodySmall = TextStyle(
        fontFamily = Helvetica,
        fontSize = 12.sp
    ),
    labelLarge = TextStyle(
        fontFamily = Helvetica,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    ),
    labelMedium = TextStyle(
        fontFamily = Helvetica,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
    ),
    labelSmall = TextStyle(
        fontFamily = Helvetica,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp
    ),
)

@Composable
fun MyAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        typography = CustomTypography,
        content = content
    )
}

@Preview(showBackground = true)
@Composable
fun ThemePreview(){
    MyAppTheme {
        Column {
            Text(
                text = "Hello, Faith",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Hello, Faith",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "Hello, Faith",
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}