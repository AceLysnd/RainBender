package com.ace.rainbender.data.local.localweather

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_weather")
data class DailyWeatherEntity(
    @PrimaryKey(autoGenerate = true)
    var dailyId: Long = 0,

    @ColumnInfo(name = "time")
    var time: String,

    @ColumnInfo(name = "temperatureMin")
    var temperatureMin: Double,

    @ColumnInfo(name = "temperatureMax")
    var temperatureMax: Double,

    @ColumnInfo(name = "weathercode")
    var weatherCode: Int
)