package com.ae.imageharamblur.utils


import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.core.graphics.createBitmap

internal fun Drawable.toBitmap(): Bitmap? {
    return when (this) {
        is BitmapDrawable -> bitmap
        else -> {
            if (intrinsicWidth <= 0 || intrinsicHeight <= 0) {
                null
            } else {
                val bitmap = createBitmap(intrinsicWidth, intrinsicHeight)
                val canvas = Canvas(bitmap)
                setBounds(0, 0, canvas.width, canvas.height)
                draw(canvas)
                bitmap
            }
        }
    }
}