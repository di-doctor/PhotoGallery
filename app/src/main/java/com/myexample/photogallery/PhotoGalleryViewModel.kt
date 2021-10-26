package com.myexample.photogallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.myexample.photogallery.api.InstanceOfFlickrApi

class PhotoGalleryViewModel : ViewModel() {

    private val flickrFetchrRepo = FlickrFetchrRepo(
        InstanceOfFlickrApi()
            .buildFlickrApi()
    ) // Создание обьекта репо
    val galleryLiveData: LiveData<List<GallaryItem>> = flickrFetchrRepo.fetchContents()  //асинхронный запрос на скачивания GSON

    override fun onCleared() {
        super.onCleared()
        flickrFetchrRepo.cancelRequest()
    }
}