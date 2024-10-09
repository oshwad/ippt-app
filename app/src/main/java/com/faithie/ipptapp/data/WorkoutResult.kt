package com.faithie.ipptapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.Date

@Entity(tableName = "workout_results")
data class WorkoutResult(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val pushUpReps: Int,
    val sitUpReps: Int,
    val date: LocalDateTime
)
