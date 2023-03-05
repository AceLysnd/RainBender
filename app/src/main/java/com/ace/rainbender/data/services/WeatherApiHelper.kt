package com.ace.rainbender.data.services

import com.ace.rainbender.ui.view.MainActivity.Companion.CURRENT_TIMEZONE
import com.ace.rainbender.ui.view.MainActivity.Companion.LATITUDE
import com.ace.rainbender.ui.view.MainActivity.Companion.LONGITUDE

class WeatherApiHelper (private val api: WeatherApiService) {
    suspend fun getWeatherForecast(
    ) = api.getWeatherForecast(
//        latitude = LATITUDE, longitude = LONGITUDE, timezone = CURRENT_TIMEZONE
    )
}