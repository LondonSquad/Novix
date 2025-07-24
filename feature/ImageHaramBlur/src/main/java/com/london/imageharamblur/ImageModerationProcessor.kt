package com.london.imageharamblur

import android.content.Context
import android.graphics.Bitmap
import com.london.imageharamblur.faceDetection.DetectedFace
import com.london.imageharamblur.faceDetection.FaceDetector
import com.london.imageharamblur.models.ContentDetectionModel
import com.london.imageharamblur.models.GenderDetectionModel
import com.london.imageharamblur.models.ModelDownloadManager
import kotlinx.coroutines.*

class ImageModerationProcessor(private val context: Context) {

    private val faceDetector = FaceDetector(context)
    private val modelDownloadManager = ModelDownloadManager(context)
    private var genderModel: GenderDetectionModel? = null
    private var contentModel: ContentDetectionModel? = null
    private var modelsInitialized = false

    private suspend fun ensureModelsLoaded() {
        if (modelsInitialized) return

        try {
            val modelFiles = modelDownloadManager.downloadModelsIfNeeded()
            genderModel = GenderDetectionModel(modelFiles.genderModelFile)
            contentModel = ContentDetectionModel(modelFiles.nsfwModelFile)
            modelsInitialized = true
        } catch (e: Exception) {
            genderModel = GenderDetectionModel(context)
            contentModel = ContentDetectionModel(context)
            modelsInitialized = true
        }
    }

    suspend fun shouldModerateImage(
        bitmap: Bitmap,
        detectFemales: Boolean = true,
        detectMales: Boolean = false,
        useContentDetection: Boolean = true
    ): Boolean = withContext(Dispatchers.Default) {
        ensureModelsLoaded()

        // Check content first
        if (useContentDetection && contentModel != null) {
            val contentResult = contentModel!!.detectContent(bitmap)
            if (contentResult.isInappropriate) {
                return@withContext true
            }
        }

        // Check faces
        val faces = faceDetector.detectFaces(bitmap)

        for (face in faces) {
            try {
                val faceBitmap = cropFace(bitmap, face)
                val genderResult = genderModel?.detectGender(faceBitmap)

                genderResult?.let { result ->
                    when {
                        detectFemales && result.isFemale -> return@withContext true
                        detectMales && !result.isFemale -> return@withContext true
                    }
                }
            } catch (_: Exception) {
                // Skip this face
            }
        }

        return@withContext false
    }

    private fun cropFace(bitmap: Bitmap, face: DetectedFace): Bitmap {
        val rect = face.boundingBox
        val left = rect.left.coerceAtLeast(0)
        val top = rect.top.coerceAtLeast(0)
        val right = rect.right.coerceAtMost(bitmap.width)
        val bottom = rect.bottom.coerceAtMost(bitmap.height)

        return Bitmap.createBitmap(bitmap, left, top, right - left, bottom - top)
    }

    fun close() {
        faceDetector.close()
        genderModel?.close()
        contentModel?.close()
    }
}