package com.ace.rainbender.data.local.localweather.daily

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface DailyWeatherDao {

    @Insert
    suspend fun insertWeather(account: DailyWeatherEntity): Long

    @Update
    suspend fun updateWeather(dailyWeatherEntity: DailyWeatherEntity)

//    @Query("SELECT * FROM account_information WHERE username = :username")
//    suspend fun getWeather(username: String) : AccountEntity

    @Query("SELECT * FROM DAILY_WEATHER WHERE dailyId == :id LIMIT 1")
    suspend fun getWeatherById(id : Long) : DailyWeatherEntity?

    @Query("SELECT * FROM DAILY_WEATHER")
    suspend fun  getAllDaily() : List<DailyWeatherEntity>

    @Query("DELETE FROM DAILY_WEATHER")
    suspend fun  deleteDatabase()
}