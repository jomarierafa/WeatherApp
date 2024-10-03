package com.jvrcoding.weatherapp.presentation.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.PixelCopy
import android.view.View
import android.view.Window
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

fun View.toBitmap(onBitmapReady: (Bitmap) -> Unit, onBitmapError: (Exception) -> Unit) {

    try {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val temporalBitmap = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888)

            // Above Android O, use PixelCopy due
            // https://stackoverflow.com/questions/58314397/
            val window: Window = (this.context as Activity).window
            val location = IntArray(2)
            this.getLocationInWindow(location)
            val viewRectangle = Rect(location[0], location[1], location[0] + this.width, location[1] + this.height)

            val onPixelCopyListener: PixelCopy.OnPixelCopyFinishedListener = PixelCopy.OnPixelCopyFinishedListener { copyResult ->
                if (copyResult == PixelCopy.SUCCESS) {
                    onBitmapReady(temporalBitmap)
                } else {
                    error("Error while copying pixels, copy result: $copyResult")
                }
            }

            PixelCopy.request(window, viewRectangle, temporalBitmap, onPixelCopyListener, Handler(
                Looper.getMainLooper()))

        } else {
            val temporalBitmap = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.RGB_565)
            val canvas = android.graphics.Canvas(temporalBitmap)
            this.draw(canvas)
            canvas.setBitmap(null)
            onBitmapReady(temporalBitmap)
        }

    } catch (exception: Exception) {

        onBitmapError(exception)
    }
}

fun shareImage(context: Context, bitmap: Bitmap) {
    val filename = "shared_image.png"
    val file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), filename)
    try {
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            out.flush()
        }

        val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context.startActivity(Intent.createChooser(shareIntent, "Share Image"))
    } catch (e: IOException) {
        Log.e("ShareError", "Error sharing image: ${e.localizedMessage}")
    }
}