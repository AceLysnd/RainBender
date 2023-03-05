package com.ace.rainbender.data.local.localweather

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DailyWeatherEntity::class], version = 4, exportSchema = false)
abstract class DailyWeatherDatabase : RoomDatabase() {

    abstract val dailyWeatherDao : DailyWeatherDao
}