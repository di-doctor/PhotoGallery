package com.myexample.photogallery

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.Message
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.myexample.photogallery.api.InstanceOfFlickrApi
import java.util.concurrent.ConcurrentHashMap

private const val TAG = "ThumbnailDownloader"
private const val MESSAGE_DOWNLOAD = 0

class ThumbnailDownloader<in T>(
    private val responseHandler: Handler,
    private val onThumbnailDownloaded: (T, Bitmap) -> Unit
) : HandlerThread(TAG), LifecycleObserver {
    private var hasQuit = false
    private lateinit var requestHandler: Handler
    override fun onLooperPrepared() {
        @SuppressLint("HandlerLeak")
        requestHandler = object : Handler(looper) {
            override fun handleMessage(msg: Message) {
                if (msg.what == MESSAGE_DOWNLOAD) {
                    val target = msg.obj as T
                    Log.i(TAG, "Got request ${requestMap[target]}")
                    handleRequest(target)
                }
            }
        }
        /* requestHandler = Handler(Looper.getMainLooper()){
             // from  Roman Andrushchenko
            return@Handler true
         }*/
    }

    private fun handleRequest(target: T) {
        val url = requestMap[target] ?: return
        val bitmap = flickrFetchrRepo.fetchPhoto(url) ?: return
        responseHandler.post(Runnable {
            if(requestMap[target] != url || hasQuit)
                return@Runnable
        requestMap.remove(target)
        onThumbnailDownloaded(target,bitmap)
        })
    }

    private val requestMap = ConcurrentHashMap<T, String>()
    private val flickrFetchrRepo = FlickrFetchrRepo(InstanceOfFlickrApi().buildFlickrApi())
    override fun quit(): Boolean {
        hasQuit = true
        return super.quit()
    }

    fun queueThumbnail(target: T, url: String) {
        Log.i(TAG, "GOT a url : $url")
        requestMap[target] = url
        requestHandler.obtainMessage(MESSAGE_DOWNLOAD, target).sendToTarget()


    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun setup() {
        Log.i(TAG, "Starting background thread")
        start()
        looper
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun tearDown() {
        Log.i(TAG, "Destroing background thread")
        quit()
    }
}