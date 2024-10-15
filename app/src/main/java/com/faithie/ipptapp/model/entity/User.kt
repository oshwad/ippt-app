package com.faithie.ipptapp.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "user_info")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val age: Int,
    val runTiming: Float, // For 2.4km run timing in minutes
    val nextIpptDate: LocalDate
)
