package com.myexample.photogallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.myexample.photogallery.api.FlickApi

class PhotoGalleryViewModel: ViewModel() {
    val galleryLiveData: LiveData<List<GallaryItem>>
    val flickrFetchrRepo: FlickrFetchrRepo

    init {
        flickrFetchrRepo = FlickrFetchrRepo()
        galleryLiveData = flickrFetchrRepo.fetchContents()
    }
    override fun onCleared() {
        super.onCleared()
        flickrFetchrRepo.flickRequest.cancel()
    }
}