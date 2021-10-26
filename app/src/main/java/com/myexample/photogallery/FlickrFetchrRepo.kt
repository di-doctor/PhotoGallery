package com.myexample.photogallery

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.myexample.photogallery.api.FlickApi
import com.myexample.photogallery.api.FlickrResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "qaz"

class FlickrFetchrRepo(private val flickrApi: FlickApi) {

    private val flickRequest: Call<FlickrResponse> = flickrApi.fetchPhoto()

     fun fetchContents(): LiveData<List<GallaryItem>> {
        val responseLiveData: MutableLiveData<List<GallaryItem>> =
            MutableLiveData()   // Создание лайв даты Вызов метода анонимного класса, создание обьекта запроса

        flickRequest.enqueue(object : Callback<FlickrResponse> {
            override fun onResponse(
                call: Call<FlickrResponse>,
                response: Response<FlickrResponse>)
            {
                var galleryItems: List<GallaryItem> =
                    response.body()?.photos?.galleryItems ?: mutableListOf()
                galleryItems = galleryItems.filterNot {
                    it.url.isBlank()
                }
                responseLiveData.value = galleryItems
            }

            override fun onFailure(call: Call<FlickrResponse>, t: Throwable) {
                Log.e(TAG, "Failed-- ${t.message}")
            }
        })
        return responseLiveData
    }

    fun cancelRequest(){            ////для вьюмодел. Отмена вызова при смерти вью модел
        flickRequest.cancel()
    }

    @WorkerThread
    fun fetchPhoto(url: String): Bitmap? {
        val response: Response<ResponseBody> = flickrApi.fetchUrlBytes(url).execute()
        val bitmap = response.body()?.byteStream()?.use(BitmapFactory::decodeStream)
        Log.d(TAG, "Decode bitmap = $bitmap from Response $response")
        return bitmap
    }
}