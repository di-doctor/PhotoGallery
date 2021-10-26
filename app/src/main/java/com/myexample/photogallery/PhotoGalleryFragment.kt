package com.myexample.photogallery

import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager

//private const val TAG = "PhotoGalleryFragment"

class PhotoGalleryFragment : Fragment() {
    private lateinit var photoRecyclerView: RecyclerView
    private lateinit var photoGalleryViewModel: PhotoGalleryViewModel
    private lateinit var thumbnailDownloader: ThumbnailDownloader<PhotoHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        photoGalleryViewModel = ViewModelProvider(this).get(PhotoGalleryViewModel::class.java)
        val responseHandler = Handler(Looper.getMainLooper())
        thumbnailDownloader =
            ThumbnailDownloader(responseHandler) { photoHolder, bitmap ->
                val drawable = BitmapDrawable(resources, bitmap)
                photoHolder.bindDrawable(drawable)
            }
        lifecycle.addObserver(thumbnailDownloader)

        val constrains = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .build()

        val workRequest = OneTimeWorkRequest
            .Builder(PollWorker::class.java)
            .setConstraints(constrains)
            .build()
        WorkManager.getInstance(requireContext())
            .enqueue(workRequest)
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(thumbnailDownloader)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_photo_gallery, container, false)
        photoRecyclerView = view.findViewById(R.id.photo_recycler_view)
        photoRecyclerView.layoutManager = GridLayoutManager(context, 3)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        photoGalleryViewModel.galleryLiveData.observe(viewLifecycleOwner, Observer { galleryItems ->
            photoRecyclerView.adapter = PhotoAdapter(galleryItems)
            Log.d("qaz", "$galleryItems")
        })
    }

    private class PhotoHolder(val itemImageView: ImageView) :
        RecyclerView.ViewHolder(itemImageView) {
        fun bindDrawable(image: Drawable) {
            itemImageView.setImageDrawable(image)
        }
        //(Drawable)->Unit = itemImageView::setImageDrawable
    }

    private inner class PhotoAdapter(private val galleryItems: List<GallaryItem>) :
        RecyclerView.Adapter<PhotoHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
            val view = layoutInflater.inflate(R.layout.list_item_galery, parent, false)
                    as ImageView
            return PhotoHolder(view)
        }

        override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
            val item = galleryItems[position]
            val placeHolderDrawable: Drawable = ContextCompat.getDrawable(
                requireContext(), R.drawable.bill_up_close
            ) ?: ColorDrawable()

            holder.bindDrawable(placeHolderDrawable)
            thumbnailDownloader.queueThumbnail(holder, item.url)
        }

        override fun getItemCount(): Int {
            return galleryItems.size
        }
    }

    companion object {
        fun newInstance() = PhotoGalleryFragment()
    }
}
