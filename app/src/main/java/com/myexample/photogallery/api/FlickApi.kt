package com.myexample.photogallery.api

import retrofit2.Call
import retrofit2.http.GET

interface FlickApi {
    @GET(
        "services/rest/?method=flickr.interestingness.getlist" +
                "&api_key=41e4ad8ff5afeac34776644158f8cfa9" +
                "&format=json" +
                "&nojsoncallback=1"+
                "&extras=url_s"
    )
    fun fetchPhoto(): Call<FlickrResponse>
}