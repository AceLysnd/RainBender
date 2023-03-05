package com.ace.rainbender.data.local.localweather.hourly

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hourly_weather")
data class HourlyWeatherEntity(
    @PrimaryKey(autoGenerate = true)
    var hourlyId: Long = 0,

    @ColumnInfo(name = "time")
    var time: String,

    @ColumnInfo(name = "temperature")
    var temperature: Double,

    @ColumnInfo(name = "humidity")
    var humidity: Int,

    @ColumnInfo(name = "weathercode")
    var weatherCode: Int
)