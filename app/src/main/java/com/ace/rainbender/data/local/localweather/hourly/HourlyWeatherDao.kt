package com.ace.rainbender.data.local.localweather.hourly

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface HourlyWeatherDao {

    @Insert
    suspend fun insertWeather(account: HourlyWeatherEntity): Long

    @Update
    suspend fun updateWeather(weatherEntity: HourlyWeatherEntity)

    @Query("SELECT * FROM HOURLY_WEATHER WHERE hourlyId == :id LIMIT 1")
    suspend fun getWeatherById(id : Long) : HourlyWeatherEntity?

    @Query("SELECT * FROM HOURLY_WEATHER")
    suspend fun  getAllDaily() : List<HourlyWeatherEntity>

    @Query("DELETE FROM HOURLY_WEATHER")
    suspend fun  deleteDatabase()
}