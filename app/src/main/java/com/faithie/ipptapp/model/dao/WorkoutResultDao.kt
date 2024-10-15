package com.faithie.ipptapp.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.faithie.ipptapp.model.entity.WorkoutResult

@Dao
interface WorkoutResultDao {
    @Insert
    suspend fun insertResult(workoutResult: WorkoutResult)

    @Query("SELECT * FROM workout_results ORDER BY date DESC")
    suspend fun getAllResults(): List<WorkoutResult>

    @Query("SELECT * FROM workout_results ORDER BY date DESC LIMIT 1")
    suspend fun getMostRecentResult(): WorkoutResult?
}