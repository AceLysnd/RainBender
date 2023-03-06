package com.ace.rainbender.data.local.localweather.daily

import javax.inject.Inject

class DailyWeatherDataSource @Inject constructor(private val dailyWeatherDao: DailyWeatherDao) {

    suspend fun getWeatherById(id: Long): DailyWeatherEntity? {
        return dailyWeatherDao.getWeatherById(id)
    }

    suspend fun getAllDaily(): List<DailyWeatherEntity> {
        return dailyWeatherDao.getAllDaily()
    }

    suspend fun insertWeather(weatherEntity: DailyWeatherEntity): Long{
        return dailyWeatherDao.insertWeather(weatherEntity)
    }

    suspend fun updateWeather(weatherEntity: DailyWeatherEntity){
        return dailyWeatherDao.updateWeather(weatherEntity)
    }

    suspend fun deleteDatabase() {
        return dailyWeatherDao.deleteDatabase()
    }

}