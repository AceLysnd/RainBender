package com.ace.rainbender.data.model.weather


import com.google.gson.annotations.SerializedName

data class Daily(
    @SerializedName("temperature_2m_max")
    val temperature2mMax: List<Double?>?,
    @SerializedName("temperature_2m_min")
    val temperature2mMin: List<Double?>?,
    @SerializedName("time")
    val time: List<String?>?,
    @SerializedName("uv_index_max")
    val uvIndexMax: List<Double?>?,
    @SerializedName("weathercode")
    val weathercode: List<Int?>?
)