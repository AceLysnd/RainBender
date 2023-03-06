package com.ace.rainbender.data.services.news

import com.ace.rainbender.data.model.news.NewsResponse
import com.ace.rainbender.data.services.news.NewsServiceBuilder.BASE_URL
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface NewsApiService {
    @GET("v2/everything")
    fun getNews(
        @Query("apiKey") apiKey: String = "da609254fcd8487a84d0b52d3afc00ff",
        @Query("q") q: String = "weather"
    ) : Call<NewsResponse>

    companion object{

        @JvmStatic
        operator fun invoke() : NewsApiService {
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
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
                .create(NewsApiService::class.java)
        }
    }
}