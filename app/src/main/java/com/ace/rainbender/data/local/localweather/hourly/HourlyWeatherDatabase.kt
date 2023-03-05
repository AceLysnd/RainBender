package com.ace.rainbender.data.local.localweather.hourly

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [HourlyWeatherEntity::class], version = 4, exportSchema = false)
abstract class HourlyWeatherDatabase : RoomDatabase() {

    abstract val hourlyWeatherDao : HourlyWeatherDao
}