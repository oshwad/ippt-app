package com.faithie.ipptapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.faithie.ipptapp.data.WorkoutDatabase
import com.faithie.ipptapp.data.WorkoutResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecordsViewModel(application: Application) :
    AndroidViewModel(application = application) {
    private val TAG = "RecordsViewModel"
    private val workoutResultDao = WorkoutDatabase.getDatabase(application).workoutResultDao()

    // MutableLiveData to store the results once fetched
    val allResults = MutableLiveData<List<WorkoutResult>>()

    init {
        fetchWorkoutResults()
    }

    // Function to fetch the data and update the MutableLiveData
    fun fetchWorkoutResults() {
        viewModelScope.launch(Dispatchers.IO) {
            val results = workoutResultDao.getAllResults()
            allResults.postValue(results)
        }
    }
}