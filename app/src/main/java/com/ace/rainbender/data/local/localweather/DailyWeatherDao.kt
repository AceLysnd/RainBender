package com.ace.rainbender.data.local.localweather

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ace.rainbender.data.model.weather.Daily

@Dao
interface DailyWeatherDao {

    @Insert
    suspend fun insertWeather(account: DailyWeatherEntity): Long

//    @Query("SELECT * FROM account_information WHERE username = :username")
//    suspend fun getWeather(username: String) : AccountEntity

    @Query("SELECT * FROM DAILY_WEATHER WHERE dailyId == :id LIMIT 1")
    suspend fun getWeatherById(id : Long) : DailyWeatherEntity?

    @Query("SELECT * FROM DAILY_WEATHER")
    suspend fun  getAllDaily() : List<DailyWeatherEntity>

    @Query("DELETE FROM DAILY_WEATHER")
    suspend fun  deleteDatabase()
}