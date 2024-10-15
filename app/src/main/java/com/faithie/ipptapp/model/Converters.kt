package com.faithie.ipptapp.model

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

class Converters {
    // Convert LocalDateTime to Long (epoch time)
    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun fromLocalDateTime(date: LocalDateTime?): Long? {
        return date?.toEpochSecond(ZoneOffset.UTC)?.times(1000) // Convert to milliseconds
    }

    // Convert Long (epoch time) to LocalDateTime
    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun toLocalDateTime(epochMillis: Long?): LocalDateTime? {
        return epochMillis?.let {
            LocalDateTime.ofEpochSecond(it / 1000, 0, ZoneOffset.UTC)
        }
    }

    // Convert LocalDate to Long (milliseconds)
    @TypeConverter
    fun fromLocalDate(localDate: LocalDate?): Long? {
        return localDate?.atStartOfDay(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
    }

    // Convert Long (milliseconds) to LocalDate
    @TypeConverter
    fun toLocalDate(millis: Long?): LocalDate? {
        return millis?.let {
            LocalDate.ofEpochDay(it / (1000 * 60 * 60 * 24))
        }
    }
}
