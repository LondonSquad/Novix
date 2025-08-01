package com.london.imageharamblur.extensions

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.core.graphics.createBitmap

internal fun Drawable.toBitmap(): Bitmap? {
    if (this is BitmapDrawable) return bitmap

    if (intrinsicWidth <= 0 || intrinsicHeight <= 0) return null

    val bitmap = createBitmap(intrinsicWidth, intrinsicHeight)
    val canvas = Canvas(bitmap)
    setBounds(0, 0, canvas.width, canvas.height)
    draw(canvas)
    return bitmap
}