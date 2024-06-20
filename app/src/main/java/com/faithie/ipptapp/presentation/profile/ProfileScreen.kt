package com.faithie.ipptapp.presentation.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ProfileScreen(profileViewModel: ProfileViewModel) {
    // UI code here
    Surface {
        Column {
            val userProfile = profileViewModel.getUserProfile()
            Text("Username: ${userProfile.username}")
            Text("Email: ${userProfile.email}")
        }
    }
}

@Composable
@Preview
fun PreviewProfileScreen() {
    // Preview code here
    ProfileScreen(ProfileViewModel(ProfileUseCase(UserRepository())))
}
