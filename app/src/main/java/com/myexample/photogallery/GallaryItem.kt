package com.myexample.photogallery

import com.google.gson.annotations.SerializedName

data class GallaryItem(
    var title: String = "",
    var id: String = "",
    @SerializedName("url_s") var url: String = ""
)
