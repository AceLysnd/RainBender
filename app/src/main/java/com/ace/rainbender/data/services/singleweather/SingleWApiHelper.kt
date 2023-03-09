package com.ace.rainbender.data.services.singleweather

import com.ace.rainbender.data.model.news.NewsResponse
import com.ace.rainbender.data.model.weather.WeatherResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SingleWApiHelper {
    fun getWeatherForecast(latitude: Double, longitude: Double, onResult: (WeatherResponse?) -> Unit){
        val retrofit = SingleWServiceBuilder.buildService(SingleWApiService::class.java)
        retrofit.getWeatherForecast(latitude, longitude).enqueue(
            object : Callback<WeatherResponse> {
                override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                    onResult(null)
                }
                override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                    val addedWeather = response.body()
                    onResult(addedWeather)
                }
            }
        )
    }
}