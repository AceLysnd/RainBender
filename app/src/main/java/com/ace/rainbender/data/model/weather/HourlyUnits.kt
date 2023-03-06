package com.ace.rainbender.data.model.weather


import com.google.gson.annotations.SerializedName

data class HourlyUnits(
    @SerializedName("relativehumidity_2m")
    val relativehumidity2m: String?,
    @SerializedName("temperature_2m")
    val temperature2m: String?,
    @SerializedName("time")
    val time: String?,
    @SerializedName("weathercode")
    val weathercode: String?
)