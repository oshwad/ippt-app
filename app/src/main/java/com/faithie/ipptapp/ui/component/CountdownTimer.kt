package com.faithie.ipptapp.ui.component

import android.media.AudioManager
import android.media.ToneGenerator
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun CountdownTimer(
    onCountdownFinished: () -> Unit
) {
    // State to keep track of countdown time
    var countdown by remember { mutableStateOf(3) }

    val tg = ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100)


    // Launch the countdown effect when the Composable enters the composition
    LaunchedEffect(countdown) {
        if (countdown > 0) {
            tg.startTone(ToneGenerator.TONE_PROP_PROMPT)
            delay(1000L) // Delay for 1 second between countdown ticks
            countdown--
        } else {
            tg.startTone(ToneGenerator.TONE_CDMA_ONE_MIN_BEEP)
            onCountdownFinished() // Notify when countdown is finished
        }
    }

    Box(
        modifier = Modifier.fillMaxSize() // Make the box fill the entire screen
    ) {
        Text(
            text = if (countdown > 0) countdown.toString() else "GO!",
            fontSize = 100.sp,
            color = if (countdown > 0) Color.Red else Color.Green,
            modifier = Modifier.align(Alignment.Center) // Center the text inside the box
        )
    }
}