package com.ace.rainbender.data.model.weather


import com.google.gson.annotations.SerializedName

data class DailyUnits(
    @SerializedName("temperature_2m_max")
    val temperature2mMax: String?,
    @SerializedName("temperature_2m_min")
    val temperature2mMin: String?,
    @SerializedName("time")
    val time: String?,
    @SerializedName("uv_index_max")
    val uvIndexMax: String?,
    @SerializedName("weathercode")
    val weathercode: String?
)