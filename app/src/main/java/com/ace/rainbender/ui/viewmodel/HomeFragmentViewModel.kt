package com.ace.rainbender.ui.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ace.rainbender.data.local.localweather.daily.DailyWeatherEntity
import com.ace.rainbender.data.local.localweather.hourly.HourlyWeatherEntity
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

    val _hourlyForecast = MutableLiveData<List<HourlyWeatherEntity>>()
    val hourlyForecast: LiveData<List<HourlyWeatherEntity>>
        get() = _hourlyForecast

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
                    val postDelayed = Handler(Looper.myLooper()!!).postDelayed({
                        loadingState.postValue(false)
                        errorState.postValue(Pair(false,null))
                    }, 3000)
                }
            } catch (e: Exception) {
                viewModelScope.launch {
                    loadingState.postValue(false)
                    errorState.postValue(Pair(true,e))
                }
            }
        }
    }

    fun getHourlyWeather(){
        loadingState.postValue(true)
        errorState.postValue(Pair(false, null))
        viewModelScope.launch {
            try {
                viewModelScope.launch {
                    _hourlyForecast.postValue(localRepository.getAllHourly())
                    val postDelayed = Handler(Looper.myLooper()!!).postDelayed({
                        loadingState.postValue(false)
                        errorState.postValue(Pair(false,null))
                    }, 500)
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