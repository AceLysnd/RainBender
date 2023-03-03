package com.ace.rainbender.data.services

import com.ace.rainbender.data.model.weather.Daily
import com.ace.rainbender.data.model.weather.Hourly
import com.ace.rainbender.data.model.weather.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherApiService {
    @GET("forecast")
    suspend fun getWeatherForecast(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("hourly") hourly: String = "temperature_2m,weathercode",
        @Query("daily") daily: String = "weathercode,temperature_2m_max,temperature_2m_min",
        @Query("timezone") timezone: String,
    ): WeatherResponse


}