package com.faithie.ipptapp.data

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

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
}
