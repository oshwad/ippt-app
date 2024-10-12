package com.faithie.ipptapp.ui.screens

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.faithie.ipptapp.data.User
import com.faithie.ipptapp.ui.component.DatePickerDocked
import com.faithie.ipptapp.ui.component.IntDropdownMenu
import com.faithie.ipptapp.ui.theme.MyAppTheme
import com.faithie.ipptapp.viewmodel.UserViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun UserDetailsFormScreen(
    navController: NavHostController,
    viewModel: UserViewModel
) {
    val TAG = "UserDetailsFormScreen"
    var name by remember { mutableStateOf("") }
    var selectedAge by remember { mutableStateOf<Int?>(null) }
    var selectedMinutes by remember { mutableStateOf<Int?>(null) }
    var selectedSeconds by remember { mutableStateOf<Int?>(null) }
    var nextIpptDate by remember { mutableStateOf<LocalDate?>(null) }

    var nameError by remember { mutableStateOf(false) }
    var ageError by remember { mutableStateOf(false) }
    var runTimingError by remember { mutableStateOf(false) }
    var ipptDateError by remember { mutableStateOf(false) }

    var isDoneClicked by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Hello!",
            style = MaterialTheme.typography.headlineMedium)
        Text(
            text = "Please enter your details",
            style = MaterialTheme.typography.labelMedium)
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = name,
            textStyle = MaterialTheme.typography.bodyMedium,
            onValueChange = {
                name = it
                nameError = it.isEmpty()
            },
            label = { Text(text = "Name", style = MaterialTheme.typography.labelMedium) },
            modifier = Modifier
                .fillMaxWidth()
        )
        if (name.isEmpty()) nameError = true
        if (nameError && isDoneClicked) {
            Text(text = "Please enter your name",
                modifier = Modifier.align(Alignment.Start),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error)
        }
        Spacer(modifier = Modifier.height(16.dp))

        val ageOptions = (18..60).toList()
        selectedAge = IntDropdownMenu(
            textLabel = "Age",
            options = ageOptions,
            initialValue = null,
            modifier = Modifier.fillMaxWidth()
        )
        if (selectedAge == null) ageError = true else ageError = false
        if (ageError && isDoneClicked) {
            Text("Please select a valid age",
                modifier = Modifier.align(Alignment.Start),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            modifier = Modifier.align(Alignment.Start),
            text = "Enter 2.4 km timing:",
            style = MaterialTheme.typography.labelMedium
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val minutesOptions = (6..20).toList()
            selectedMinutes = IntDropdownMenu(
                textLabel = "Minutes",
                options = minutesOptions,
                initialValue = null,
                modifier = Modifier.width(180.dp)
            )

            Spacer(modifier = Modifier.width(5.dp))

            val secondsOptions = (0..59).toList()
            selectedSeconds = IntDropdownMenu(
                textLabel = "Seconds",
                options = secondsOptions,
                initialValue = null,
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (selectedMinutes == null || selectedSeconds == null) {
            runTimingError = true
            if (isDoneClicked) {
                Text("Please select both minutes and seconds",
                    modifier = Modifier.align(Alignment.Start),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error)
            }
        } else {
            runTimingError = false
        }

        Spacer(modifier = Modifier.height(16.dp))
        nextIpptDate = DatePickerDocked("")
        if (nextIpptDate == null) {
            ipptDateError = true
            if (isDoneClicked) {
                Text("Please select a valid IPPT date",
                    modifier = Modifier.align(Alignment.Start),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error)
            }
        } else {
            ipptDateError = false
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            if (!nameError && !ageError && !runTimingError && !ipptDateError) {
                coroutineScope.launch {
                    val runTiming = selectedMinutes!! + selectedSeconds!! / 60f
                    val user = User(
                        name = name,
                        age = selectedAge!!,
                        runTiming = runTiming,
                        nextIpptDate = nextIpptDate!!
                    )
                    viewModel.insertUser(user)
                    navController.navigate(Screens.Home.route)
                }
            } else {
                isDoneClicked = true
            }
        }) {
            Text(text = "Done", style = MaterialTheme.typography.labelMedium)
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun UserFormPreview() {
//    MyAppTheme {
//        UserDetailsFormScreen(rememberNavController())
//    }
//}