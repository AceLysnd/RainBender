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

    @GET("forecast?latitude=-7.76&longitude=110.43&hourly=temperature_2m,relativehumidity_2m,weathercode&daily=weathercode,temperature_2m_max,temperature_2m_min,uv_index_max&timezone=Asia%2FBangkok")
    suspend fun getWeatherForecast(
//        @Query("latitude") latitude: Double,
//        @Query("longitude") longitude: Double,
//        @Query("hourly") hourly: String = "temperature_2m,weathercode",
//        @Query("daily") daily: String = "weathercode,temperature_2m_max,temperature_2m_min",
//        @Query("timezone") timezone: String = "Asia/Bangkok",
    ): WeatherResponse

    companion object{

        @JvmStatic
        operator fun invoke() : WeatherApiService{
            val authInterceptor = Interceptor{
                val originRequest = it.request()
                val newUrl = originRequest.url.newBuilder().apply {
                }.build()
                it.proceed(originRequest.newBuilder().url(newUrl).build())
            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .baseUrl("https://api.open-meteo.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
                .create(WeatherApiService::class.java)
        }
    }


}