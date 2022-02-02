package com.robinsmorton.rmappandroid.util

import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import java.net.URL
import java.util.concurrent.Executors

object ImageUtil {

    fun loadImageFromUrl(url: String?, imageView: ImageView) {
        Executors.newSingleThreadExecutor().execute {
            try {
                val inputStream = URL(url).openStream()
                val imageBitmap = BitmapFactory.decodeStream(inputStream)
                Log.d("ImageUtil", "*** - "+imageBitmap.config)
                Handler(Looper.getMainLooper()).post {
                    imageView.setImageBitmap(imageBitmap)
                }
            } catch(e: Exception) {
                e.printStackTrace()
            }
        }

    }
}