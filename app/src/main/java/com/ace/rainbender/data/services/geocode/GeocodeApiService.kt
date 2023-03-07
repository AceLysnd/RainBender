package com.ace.rainbender.data.services.geocode

import com.ace.rainbender.data.model.geocoding.GeocodeResponse
import com.ace.rainbender.data.model.news.NewsResponse
import com.ace.rainbender.data.services.news.NewsApiService
import com.ace.rainbender.data.services.news.NewsServiceBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface GeocodeApiService {
    @GET("v1/search")
    fun getGeocodeResult(
        @Query("name") name: String
    ) : Call<GeocodeResponse>

    companion object{

        @JvmStatic
        operator fun invoke() : GeocodeApiService {
            val authInterceptor = Interceptor{
                val originRequest = it.request()
                val newUrl = originRequest.url.newBuilder().apply {
                }.build()
                it.proceed(originRequest.newBuilder().url(newUrl).build())
            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .baseUrl(GeocodeServiceBuilder.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
                .create(GeocodeApiService::class.java)
        }
    }
}