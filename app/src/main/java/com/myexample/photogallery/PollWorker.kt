package com.myexample.photogallery

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

private const val TAG = "PoolWorker"

class PollWorker(val contex: Context, workerParameters: WorkerParameters): Worker(contex, workerParameters) {
    override fun doWork(): Result {
       Log.i(TAG, "work request triggered")
        return Result.success()
    }
}