package com.ace.rainbender.data.services.singleweather

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SingleWServiceBuilder {
    private val okHttpClient = OkHttpClient.Builder().build()
    val BASE_URL = "https://api.open-meteo.com/v1/"
    private val retrofit = Retrofit.Builder()

        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    fun <T> buildService(service: Class<T>): T {
        return retrofit.create(service)
    }
}