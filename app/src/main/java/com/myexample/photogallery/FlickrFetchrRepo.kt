package com.myexample.photogallery

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.myexample.photogallery.api.FlickApi
import com.myexample.photogallery.api.FlickrResponse
import com.myexample.photogallery.api.PhotoResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val TAG = "FlickrFetchr"

class FlickrFetchrRepo {
    val flickRequest: Call<FlickrResponse> //для вьюмодел. Отмена вызова при смерти вью модел

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://api.flickr.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val flickrApi = retrofit.create(FlickApi::class.java)  //обьект анонимного класса реализующий интерфейс FlickApi
        flickRequest = flickrApi.fetchPhoto()
    }

    fun fetchContents(): LiveData<List<GallaryItem>> {
        val responseLiveData: MutableLiveData<List<GallaryItem>> = MutableLiveData()   // Создание лайв даты
           //Вызов метода анонимного класса, создание обьекта запроса

        flickRequest.enqueue(object : Callback<FlickrResponse> {
            override fun onResponse(call: Call<FlickrResponse>, response: Response<FlickrResponse>) {
                val flickrResponse: FlickrResponse? = response.body()
                val photoResponse:PhotoResponse? = flickrResponse?.photos
                var galaryItems: List<GallaryItem> = photoResponse?.galleryItems?: mutableListOf()
                galaryItems = galaryItems.filterNot {
                    it.url.isBlank()
                }
                responseLiveData.value  = galaryItems
            }
            override fun onFailure(call: Call<FlickrResponse>, t: Throwable) {
                Log.e(TAG, "Failed-- ${t.message}")
            }
        })
        return responseLiveData
    }
}