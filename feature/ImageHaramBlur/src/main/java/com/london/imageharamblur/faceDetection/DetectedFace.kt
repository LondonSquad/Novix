package com.london.imageharamblur.faceDetection
import android.graphics.Rect

data class DetectedFace(
    val boundingBox: Rect,
    val confidence: Float
)