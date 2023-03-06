package com.ace.rainbender.data.services

import com.ace.rainbender.data.model.news.NewsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsApiHelper {
    fun getNews(onResult: (NewsResponse?) -> Unit){
        val retrofit = NewsServiceBuilder.buildService(NewsApiService::class.java)
        retrofit.getNews().enqueue(
            object : Callback<NewsResponse> {
                override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                    onResult(null)
                }
                override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                    val addedUser = response.body()
                    onResult(addedUser)
                }
            }
        )
    }
}