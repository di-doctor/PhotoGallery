package com.myexample.photogallery.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class InstanceOfFlickrApi {
    private fun createRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.flickr.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun buildFlickrApi(): FlickApi {
        val retrofit: Retrofit = createRetrofit()
        return retrofit.create(FlickApi::class.java)
    }
}