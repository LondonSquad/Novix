package com.ae.imageharamblur

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import com.ae.imageharamblur.faceDetection.FaceDetector
import com.ae.imageharamblur.models.ContentDetectionModel
import com.ae.imageharamblur.models.GenderDetectionModel
import com.ae.imageharamblur.models.ModelDownloadManager
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import com.ae.imageharamblur.faceDetection.DetectedFace

class ImageModerationProcessor(private val context: Context) {

    private val faceDetector = FaceDetector(context)
    private val modelDownloadManager = ModelDownloadManager(context)

    private var genderModel: GenderDetectionModel? = null
    private var contentModel: ContentDetectionModel? = null
    private var modelsInitialized = false

    private val mutex = Mutex()
    private var activeJob: Job? = null

    private suspend fun ensureModelsLoaded() {
        if (modelsInitialized) return

        mutex.withLock {
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
    }

    suspend fun processImage(
        bitmap: Bitmap,
        detectFemales: Boolean = true,
        detectMales: Boolean = false,
        useContentDetection: Boolean = true,
        strictMode: Boolean = false
    ): ProcessingResult = withContext(Dispatchers.Default) {
        ensureModelsLoaded()

        mutex.withLock {
            activeJob = coroutineContext[Job]
        }

        try {
            val contentDeferred = async {
                if (useContentDetection && contentModel != null)
                    contentModel!!.detectContent(bitmap)
                else null
            }

            val facesDeferred = async {
                faceDetector.detectFaces(bitmap)
            }

            val contentResult = contentDeferred.await()
            val faces = facesDeferred.await()
            val faceInfoList = mutableListOf<FaceInfo>()

            if (contentResult != null && contentResult.isInappropriate)
                return@withContext ProcessingResult(
                    shouldModerate = true,
                    reason = "Inappropriate content detected",
                    details = ProcessingDetails(
                        contentScore = contentResult.score,
                        isInappropriate = true,
                        faceRegions = faceInfoList
                    )
                )

            var femaleCount = 0
            var maleCount = 0
            var uncertainCount = 0

            faces.forEach { face ->
                try {
                    val faceBitmap = cropFace(bitmap, face)
                    val genderResult = genderModel?.detectGender(faceBitmap)

                    genderResult?.let { result ->
                        val gender = when {
                            result.confidence < DEFAULT_GENDER_CONFIDENCE_THRESHOLD -> {
                                uncertainCount++
                                if (strictMode) femaleCount++
                                Gender.UNCERTAIN
                            }

                            result.isFemale -> {
                                femaleCount++
                                Gender.FEMALE
                            }

                            else -> {
                                maleCount++
                                Gender.MALE
                            }
                        }

                        faceInfoList.add(
                            FaceInfo(
                                boundingBox = face.boundingBox,
                                gender = gender,
                                confidence = result.confidence
                            )
                        )
                    }
                } catch (_: Exception) {
                    // Skip this face
                }
            }

            val shouldModerate = when {
                detectFemales && femaleCount > 0 -> true
                detectMales && maleCount > 0 -> true
                strictMode && uncertainCount > 0 -> true
                else -> false
            }

            val reason = when {
                femaleCount > 0 && detectFemales -> "Detected $femaleCount female face(s)"
                maleCount > 0 && detectMales -> "Detected $maleCount male face(s)"
                uncertainCount > 0 && strictMode -> "Uncertain detection in strict mode"
                else -> null
            }

            ProcessingResult(
                shouldModerate = shouldModerate,
                reason = reason,
                details = ProcessingDetails(
                    facesDetected = faces.size,
                    femalesDetected = femaleCount,
                    malesDetected = maleCount,
                    contentScore = contentResult?.score ?: 0f,
                    isInappropriate = contentResult?.isInappropriate == true,
                    faceRegions = faceInfoList
                )
            )
        } finally {
            mutex.withLock {
                activeJob = null
            }
        }
    }

    private fun cropFace(bitmap: Bitmap, face: DetectedFace): Bitmap {
        val rect = face.boundingBox
        val left = (rect.left).coerceAtLeast(0)
        val top = (rect.top).coerceAtLeast(0)
        val right = (rect.right).coerceAtMost(bitmap.width)
        val bottom = (rect.bottom).coerceAtMost(bitmap.height)

        val width = right - left
        val height = bottom - top

        return Bitmap.createBitmap(bitmap, left, top, width, height)
    }

    fun close() {
        runBlocking {
            mutex.withLock {
                activeJob?.cancelAndJoin()
            }
        }
        faceDetector.close()
        genderModel?.close()
        contentModel?.close()
    }

    data class ProcessingResult(
        val shouldModerate: Boolean,
        val reason: String? = null,
        val details: ProcessingDetails? = null
    )

    data class ProcessingDetails(
        val facesDetected: Int = 0,
        val femalesDetected: Int = 0,
        val malesDetected: Int = 0,
        val contentScore: Float = 0f,
        val isInappropriate: Boolean = false,
        val faceRegions: List<FaceInfo> = emptyList()
    )

    data class FaceInfo(
        val boundingBox: Rect,
        val gender: Gender,
        val confidence: Float
    )

    enum class Gender {
        MALE, FEMALE, UNCERTAIN
    }

    companion object {
        const val DEFAULT_CONTENT_THRESHOLD = 0.3f
        private const val DEFAULT_GENDER_CONFIDENCE_THRESHOLD = 0.5f
        private const val FACE_CROP_PADDING = 0.15f
    }
}