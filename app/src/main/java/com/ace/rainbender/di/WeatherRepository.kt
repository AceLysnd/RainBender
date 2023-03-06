package com.ace.rainbender.di

import com.ace.rainbender.data.services.WeatherApiHelper
import com.ace.rainbender.ui.view.MainActivity.Companion.LATITUDE
import com.ace.rainbender.ui.view.MainActivity.Companion.LONGITUDE

class WeatherRepository(private val weatherApiHelper: WeatherApiHelper) {
    suspend fun getWeatherForecast() = weatherApiHelper.getWeatherForecast(latitude = LATITUDE, longitude = LONGITUDE)
}