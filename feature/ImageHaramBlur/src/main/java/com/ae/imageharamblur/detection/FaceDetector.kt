package com.ae.imageharamblur.detection

import android.graphics.Bitmap
import android.graphics.Rect
import android.content.Context
import android.util.Log

internal class FaceDetector(context: Context) {

    private val blazeFaceDetector = BlazeFaceDetector(context)

    fun detectFaces(bitmap: Bitmap): List<DetectedFace> {
        return try {
            val blazeFaceResults = blazeFaceDetector.detectFaces(bitmap)

            // Convert BlazeFace results to FaceDetector.DetectedFace
            blazeFaceResults.map { blazeFace ->
                DetectedFace(
                    boundingBox = blazeFace.boundingBox
                )
            }
        } catch (e: Exception) {
            Log.e("FaceDetector", "Error detecting faces", e)
            emptyList()
        }
    }

    fun close() {
        blazeFaceDetector.close()
    }

    data class DetectedFace(val boundingBox: Rect)
}