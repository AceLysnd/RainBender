package com.ace.rainbender.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ace.rainbender.data.local.localweather.DailyWeatherEntity
import com.ace.rainbender.data.local.localweather.DailyWeatherList
import com.ace.rainbender.data.model.weather.WeatherResponse
import com.ace.rainbender.di.LocalRepository
import com.ace.rainbender.di.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val localRepository: LocalRepository
    ) : ViewModel() {

    val loadingState = MutableLiveData<Boolean>()
    val errorState = MutableLiveData<Pair<Boolean, Exception?>>()

    val _weatherForecast = MutableLiveData<WeatherResponse>()
    val weatherForecast: LiveData<WeatherResponse>
        get() = _weatherForecast

    val _dailyForecast = MutableLiveData<List<DailyWeatherEntity>>()
    val dailyForecast: LiveData<List<DailyWeatherEntity>>
        get() = _dailyForecast

    fun getWeatherForecast(){
        viewModelScope.launch {
            _weatherForecast.postValue(weatherRepository.getWeatherForecast())
        }
    }

    fun getDailyWeather(){
        loadingState.postValue(true)
        errorState.postValue(Pair(false, null))
        viewModelScope.launch {
            try {
                viewModelScope.launch {
                    _dailyForecast.postValue(localRepository.getAllDaily())
                    loadingState.postValue(false)
                    errorState.postValue(Pair(false,null))
                }
            } catch (e: Exception) {
                viewModelScope.launch {
                    loadingState.postValue(false)
                    errorState.postValue(Pair(true,e))
                }
            }
        }
    }
}