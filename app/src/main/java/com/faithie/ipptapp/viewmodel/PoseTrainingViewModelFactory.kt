package com.faithie.ipptapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PoseTrainingViewModelFactory(private val application: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PoseTrainingViewModel::class.java)) {
            return PoseTrainingViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}