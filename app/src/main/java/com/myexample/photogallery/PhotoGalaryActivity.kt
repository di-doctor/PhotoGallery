package com.myexample.photogallery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.myexample.photogallery.databinding.ActivityPhotoGalleryBinding

class PhotoGalaryActivity : AppCompatActivity() {
lateinit var binding: ActivityPhotoGalleryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityPhotoGalleryBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val isFragmentContainerEmpty = savedInstanceState == null
        if (isFragmentContainerEmpty) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragmentContainer, PhotoGalleryFragment.newInstance()) //куда и что
                .commit()
        }
    }
}