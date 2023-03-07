package com.ace.rainbender.data.services.geocode

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GeocodeServiceBuilder {
    private val okHttpClient = OkHttpClient.Builder().build()
    val BASE_URL = "https://geocoding-api.open-meteo.com/"
    private val retrofit = Retrofit.Builder()

        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    fun <T> buildService(service: Class<T>): T {
        return retrofit.create(service)
    }
}