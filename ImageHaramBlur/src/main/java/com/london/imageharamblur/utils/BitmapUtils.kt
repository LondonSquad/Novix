package com.london.imageharamblur.utils

import android.graphics.Bitmap
import com.london.imageharamblur.faceDetection.DetectedFace

fun cropFace(bitmap: Bitmap, face: DetectedFace): Bitmap {
    val rect = face.boundingBox
    val left = rect.left.coerceAtLeast(0)
    val top = rect.top.coerceAtLeast(0)
    val right = rect.right.coerceAtMost(bitmap.width)
    val bottom = rect.bottom.coerceAtMost(bitmap.height)

    return Bitmap.createBitmap(bitmap, left, top, right - left, bottom - top)
}