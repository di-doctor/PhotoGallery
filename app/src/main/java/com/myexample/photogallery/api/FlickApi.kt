package com.myexample.photogallery.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface FlickApi {
    @GET(
        "services/rest/?method=flickr.interestingness.getlist" +
                "&api_key=41e4ad8ff5afeac34776644158f8cfa9" +
                "&format=json" +
                "&nojsoncallback=1" +
                "&extras=url_s"
    )
    fun fetchPhoto(): Call<FlickrResponse>

    @GET
    fun fetchUrlBytes(@Url url: String): Call<ResponseBody>
}