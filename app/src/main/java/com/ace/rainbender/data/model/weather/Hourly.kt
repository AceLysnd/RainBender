package com.ace.rainbender.data.model.weather


import com.google.gson.annotations.SerializedName

data class Hourly(
    @SerializedName("relativehumidity_2m")
    val relativehumidity2m: List<Int?>?,
    @SerializedName("temperature_2m")
    val temperature2m: List<Double?>?,
    @SerializedName("time")
    val time: List<String?>?,
    @SerializedName("weathercode")
    val weathercode: List<Int?>?
)