package com.ace.rainbender.di

import com.ace.rainbender.data.services.WeatherApiHelper

class WeatherRepository(private val weatherApiHelper: WeatherApiHelper) {
    suspend fun getWeatherForecast() = weatherApiHelper.getWeatherForecast()
}