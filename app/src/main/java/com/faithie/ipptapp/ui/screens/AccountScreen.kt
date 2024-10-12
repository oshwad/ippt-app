package com.faithie.ipptapp.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.faithie.ipptapp.data.User
import com.faithie.ipptapp.ui.component.DatePickerDocked
import com.faithie.ipptapp.ui.component.IntDropdownMenu
import com.faithie.ipptapp.viewmodel.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun AccountScreen(
    navController: NavHostController,
    viewModel: UserViewModel
) {
    val TAG = "AccountScreen"
    val context = LocalContext.current
    var userState = remember { mutableStateOf<User?>(null) }
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf(20) }
    var runTiming by remember { mutableStateOf(0f) }
    var nextIpptDate by remember { mutableStateOf<LocalDate?>(null) }
    var isUserDataLoaded by remember { mutableStateOf(false) }

    var selectedAge by remember { mutableStateOf<Int?>(null) }
    var selectedMinutes by remember { mutableStateOf<Int?>(null) }
    var selectedSeconds by remember { mutableStateOf<Int?>(null) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }

    var triggerLaunchedEffect by remember { mutableStateOf(0) }

    LaunchedEffect(triggerLaunchedEffect) {
        val user = viewModel.getUser()
        userState.value = user
        Log.d(TAG, "User: ${userState.value}")

        user?.let {
            name = it.name
            age = it.age
            runTiming = it.runTiming
            nextIpptDate = it.nextIpptDate
            isUserDataLoaded = true

            selectedAge = age
            val (minutes, seconds) = floatToMinutesAndSeconds(runTiming)
            selectedMinutes = minutes
            selectedSeconds = seconds
            selectedDate = nextIpptDate
        }

        Log.d(TAG, "Name: $name, Age: $age, RunTiming: $runTiming, IPPTDate: $nextIpptDate")
    }

    var nameError by remember { mutableStateOf(false) }
    var isUpdateClicked by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Account Details",
            style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        Log.d(TAG, "Name in composable: $name")
        TextField(
            value = name,
            textStyle = MaterialTheme.typography.bodyMedium,
            onValueChange = {
                name = it
                nameError = it.isEmpty()
            },
            label = { Text("Name", style = MaterialTheme.typography.labelMedium) },
            modifier = Modifier
                .fillMaxWidth()
        )
//        if (name.isEmpty()) nameError = true
        if (nameError && isUpdateClicked) {
            Text(text = "Name cannot be empty",
                modifier = Modifier.align(Alignment.Start),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error)
        }
        Spacer(modifier = Modifier.height(16.dp))

        if (isUserDataLoaded) {
            Log.d(TAG, "Age in composable: $age")
            val ageOptions = (18..60).toList()
            selectedAge = IntDropdownMenu(
                textLabel = "Age",
                options = ageOptions,
                initialValue = age,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                modifier = Modifier.align(Alignment.Start),
                text = "2.4 km Timing:",
                style = MaterialTheme.typography.labelMedium
            )

            val (minutes, seconds) = floatToMinutesAndSeconds(runTiming)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val minutesOptions = (6..20).toList()
                selectedMinutes = IntDropdownMenu(
                    textLabel = "Minutes",
                    options = minutesOptions,
                    initialValue = minutes,
                    modifier = Modifier.width(180.dp)
                )

                Spacer(modifier = Modifier.width(5.dp))

                val secondsOptions = (0..59).toList()
                selectedSeconds = IntDropdownMenu(
                    textLabel = "Seconds",
                    options = secondsOptions,
                    initialValue = seconds,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            selectedDate = DatePickerDocked(nextIpptDate?.let { localDateToString(it) })
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                isUpdateClicked = true

                if (selectedAge == null || selectedMinutes == null || selectedSeconds == null || selectedDate == null) {
                    Log.d(TAG, "Some fields are null")
                    return@Button
                }

                // Values check (compare with the initial values)
                val finalAge = selectedAge!!
                val finalRunTiming = minutesAndSecondsToFloat(selectedMinutes!!, selectedSeconds!!)
                val finalIpptDate = selectedDate!!

                val noChange = name == userState.value?.name &&
                        finalAge == userState.value?.age &&
                        finalRunTiming == userState.value?.runTiming &&
                        finalIpptDate == userState.value?.nextIpptDate

                if (noChange) {
                    Log.d(TAG, "No changes detected")
                    // No update is required, exit early
                    return@Button
                }

                // Proceed with updating the user
                val updatedUser = User(
                    name = name,
                    age = finalAge,
                    runTiming = finalRunTiming,
                    nextIpptDate = finalIpptDate
                )

                // Update the user in the database
                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.updateUser(updatedUser)

                    CoroutineScope(Dispatchers.Main).launch {
                        Toast.makeText(context, "User updated successfully", Toast.LENGTH_SHORT).show()
                        triggerLaunchedEffect++
                    }
                }
            }
        ) {
            Text("Update", style = MaterialTheme.typography.labelMedium)
        }
    }
}

fun floatToMinutesAndSeconds(timeInMinutes: Float): Pair<Int, Int> {
    val totalSeconds = (timeInMinutes * 60).toInt()
    val minutes: Int = totalSeconds / 60
    val seconds: Int = totalSeconds % 60
    return Pair(minutes, seconds)
}

fun minutesAndSecondsToFloat(min: Int, sec: Int): Float {
    return (min * 60 + sec).toFloat() / 60
}

fun localDateToString(date: LocalDate): String {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    return date.format(formatter)
}