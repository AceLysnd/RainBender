package com.ace.rainbender.data.services.geocode

import com.ace.rainbender.data.model.geocoding.GeocodeResponse
import com.ace.rainbender.data.model.news.NewsResponse
import com.ace.rainbender.data.services.news.NewsApiService
import com.ace.rainbender.data.services.news.NewsServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GeocodeApiHelper {
    fun getGeocodeResponse(query: String, onResult: (GeocodeResponse?) -> Unit){
        val retrofit = GeocodeServiceBuilder.buildService(GeocodeApiService::class.java)
        retrofit.getGeocodeResult(query).enqueue(
            object : Callback<GeocodeResponse> {
                override fun onFailure(call: Call<GeocodeResponse>, t: Throwable) {
                    onResult(null)
                }
                override fun onResponse(call: Call<GeocodeResponse>, response: Response<GeocodeResponse>) {
                    val addedGeocode = response.body()
                    onResult(addedGeocode)
                }
            }
        )
    }
}