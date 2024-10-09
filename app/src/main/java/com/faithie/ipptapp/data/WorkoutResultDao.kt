package com.faithie.ipptapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WorkoutResultDao {
    @Insert
    suspend fun insertResult(exerciseResult: WorkoutResult)

    @Query("SELECT * FROM workout_results ORDER BY date DESC")
    suspend fun getAllResults(): List<WorkoutResult>
}