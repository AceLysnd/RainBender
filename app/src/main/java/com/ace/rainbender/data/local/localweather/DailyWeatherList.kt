package com.ace.rainbender.data.local.localweather

import com.google.gson.annotations.SerializedName

data class DailyWeatherList (
    @SerializedName("daily_weather") val dailyWeather: MutableList<DailyWeatherEntity>
)