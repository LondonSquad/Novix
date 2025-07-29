package com.london.imageharamblur

import android.content.Context
import android.graphics.Bitmap
import com.london.imageharamblur.faceDetection.DetectedFace
import com.london.imageharamblur.faceDetection.FaceDetector
import com.london.imageharamblur.models.ContentDetectionModel
import com.london.imageharamblur.models.GenderDetectionModel
import com.london.imageharamblur.models.ModelDownloadManager
import com.london.imageharamblur.ui.ModerationCacheManager
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal class ImageModerationProcessor(private val context: Context) {
    companion object {
        private val initMutex = Mutex()

        @Volatile
        private var sharedGenderModel: GenderDetectionModel? = null

        @Volatile
        private var sharedContentModel: ContentDetectionModel? = null

        @Volatile
        private var modelsInitialized = false

        @Volatile
        private var activeProcessorCount = 0

        @Volatile
        private var currentModelsAreFromFirebase = false

        private val modelChangeListeners = mutableListOf<() -> Unit>()

        private fun notifyModelChange() {
            modelChangeListeners.forEach { it.invoke() }
        }
    }

    private val faceDetector = FaceDetector(context)
    private val modelDownloadManager = ModelDownloadManager(context)

    init {
        synchronized(ImageModerationProcessor::class.java) {
            activeProcessorCount++
        }
    }

    private suspend fun ensureModelsLoaded() {
        if (modelsInitialized && sharedGenderModel != null && sharedContentModel != null) {
            return
        }

        initMutex.withLock {
            if (modelsInitialized && sharedGenderModel != null && sharedContentModel != null) {
                return
            }

            runCatching {
                val modelFiles = modelDownloadManager.downloadModelsIfNeeded()
                val switchingToFirebase =
                    !currentModelsAreFromFirebase && modelFiles.isFromFirebase && modelsInitialized

                if (switchingToFirebase) {
                    sharedGenderModel?.close()
                    sharedContentModel?.close()
                    sharedGenderModel = null
                    sharedContentModel = null
                    modelsInitialized = false
                }

                if (modelFiles.isFromFirebase) {
                    sharedGenderModel = GenderDetectionModel(modelFiles.genderModelFile)
                    sharedContentModel = ContentDetectionModel(modelFiles.nsfwModelFile)
                    currentModelsAreFromFirebase = true

                    if (switchingToFirebase) {
                        ModerationCacheManager.clear()
                        notifyModelChange()
                    }
                } else {
                    sharedGenderModel = GenderDetectionModel(context)
                    sharedContentModel = ContentDetectionModel(context)
                    currentModelsAreFromFirebase = false
                }

                modelsInitialized = true
            }.onFailure {
                if (currentModelsAreFromFirebase) {
                    runCatching {
                        sharedGenderModel?.close()
                        sharedContentModel?.close()

                        sharedGenderModel = GenderDetectionModel(context)
                        sharedContentModel = ContentDetectionModel(context)
                        currentModelsAreFromFirebase = false
                        modelsInitialized = true
                    }
                }
            }
        }
    }

    suspend fun shouldModerateImage(
        bitmap: Bitmap,
        detectFemales: Boolean = true,
        detectMales: Boolean = false,
        useContentDetection: Boolean = true
    ): Boolean = withContext(Dispatchers.Default) {
        ensureModelsLoaded()

        if (useContentDetection) {
            runCatching {
                val contentModel = sharedContentModel
                if (contentModel != null) {
                    val contentResult = contentModel.detectContent(bitmap)
                    if (contentResult.isInappropriate) {
                        return@withContext true
                    }
                }
            }
        }

        runCatching {
            val faces = faceDetector.detectFaces(bitmap)

            for (face in faces) {
                runCatching {
                    val faceBitmap = cropFace(bitmap, face)
                    val genderModel = sharedGenderModel

                    if (genderModel != null) {
                        val genderResult = genderModel.detectGender(faceBitmap)

                        genderResult.let { result ->
                            if ((detectFemales && result.isFemale) || (detectMales && !result.isFemale))
                                return@withContext true
                        }
                    }
                }
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
        synchronized(ImageModerationProcessor::class.java) {
            activeProcessorCount--

            if (activeProcessorCount == 0) {
                runCatching { faceDetector.close() }
                runCatching { sharedGenderModel?.close() }
                sharedGenderModel = null
                runCatching { sharedContentModel?.close() }
                sharedContentModel = null

                modelsInitialized = false
                currentModelsAreFromFirebase = false
            } else {
                runCatching { faceDetector.close() }
            }
        }
    }
}