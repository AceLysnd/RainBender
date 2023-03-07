package com.ace.rainbender.data.model.geocoding


import com.google.gson.annotations.SerializedName

data class GeocodeResponse(
    @SerializedName("generationtime_ms")
    val generationtimeMs: Double?,
    @SerializedName("results")
    val results: List<Result?>?
)