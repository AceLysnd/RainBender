package com.ace.rainbender.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ace.rainbender.data.local.localweather.DailyWeatherEntity
import com.ace.rainbender.data.model.weather.WeatherResponse
import com.ace.rainbender.di.LocalRepository
import com.ace.rainbender.di.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val localRepository: LocalRepository
    ) : ViewModel() {

    val _weatherForecast = MutableLiveData<WeatherResponse>()
    val weatherForecast: LiveData<WeatherResponse>
        get() = _weatherForecast

    fun getWeatherForecast(){
        viewModelScope.launch {
            _weatherForecast.postValue(weatherRepository.getWeatherForecast())
        }
    }

    fun insertDailyWeather(dailyWeatherEntity: DailyWeatherEntity){
        viewModelScope.launch {
            localRepository.insertDailyWeather(dailyWeatherEntity)
        }
    }

    fun deleteDailyWeather() {
        viewModelScope.launch {
            localRepository.deleteDailyDatabase()
        }
    }
}