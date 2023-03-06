package com.ace.rainbender.data.services

import com.ace.rainbender.data.model.weather.WeatherResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface WeatherApiService {

    @GET("forecast")
    suspend fun getWeatherForecast(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("hourly") hourly: Array<String> = arrayOf("temperature_2m","relativehumidity_2m","weathercode"),
        @Query("daily") daily: Array<String> = arrayOf("weathercode", "temperature_2m_max", "temperature_2m_min", "uv_index_max"),
        @Query("timezone") timezone: String = "Asia/Bangkok"
    ): WeatherResponse


}