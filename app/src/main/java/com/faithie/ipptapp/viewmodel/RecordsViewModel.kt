package com.faithie.ipptapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.faithie.ipptapp.model.database.WorkoutDatabase
import com.faithie.ipptapp.model.entity.WorkoutResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecordsViewModel(application: Application) :
    AndroidViewModel(application = application) {
    private val TAG = "RecordsViewModel"
    private val workoutResultDao = WorkoutDatabase.getDatabase(application).workoutResultDao()

    val allResults = MutableLiveData<List<WorkoutResult>>()
    val mostRecentWorkout = MutableLiveData<WorkoutResult>()

    init {
        fetchWorkoutResults()
    }

    // Function to fetch the data and update the MutableLiveData
    fun fetchWorkoutResults() {
        viewModelScope.launch(Dispatchers.IO) {
            val results = workoutResultDao.getAllResults()
            allResults.postValue(results)

            mostRecentWorkout.postValue(workoutResultDao.getMostRecentResult())
        }
    }
}