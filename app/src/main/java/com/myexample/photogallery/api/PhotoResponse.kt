package com.myexample.photogallery.api

import com.google.gson.annotations.SerializedName
import com.myexample.photogallery.GallaryItem

class PhotoResponse {
    @SerializedName("photo")
    lateinit var galleryItems: List<GallaryItem>
}