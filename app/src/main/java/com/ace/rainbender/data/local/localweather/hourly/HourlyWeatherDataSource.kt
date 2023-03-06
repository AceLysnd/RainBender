package com.ace.rainbender.data.local.localweather.hourly

import javax.inject.Inject

class HourlyWeatherDataSource @Inject constructor(private val hourlyWeatherDao: HourlyWeatherDao) {

    suspend fun getWeatherById(id: Long): HourlyWeatherEntity? {
        return hourlyWeatherDao.getWeatherById(id)
    }

    suspend fun getAllHourly(): List<HourlyWeatherEntity> {
        return hourlyWeatherDao.getAllDaily()
    }

    suspend fun insertWeather(weatherEntity: HourlyWeatherEntity): Long{
        return hourlyWeatherDao.insertWeather(weatherEntity)
    }

    suspend fun updateWeather(weatherEntity: HourlyWeatherEntity){
        return hourlyWeatherDao.updateWeather(weatherEntity)
    }

    suspend fun deleteDatabase() {
        return hourlyWeatherDao.deleteDatabase()
    }
}