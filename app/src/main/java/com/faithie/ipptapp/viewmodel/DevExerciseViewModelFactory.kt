package com.faithie.ipptapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DevExerciseViewModelFactory(private val application: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DevExerciseViewModel::class.java)) {
            return DevExerciseViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}