package com.ae.imageharamblur

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.util.Log
import com.ae.imageharamblur.detection.FaceDetector
import com.ae.imageharamblur.models.ContentDetectionModel
import com.ae.imageharamblur.models.GenderDetectionModel
import com.ae.imageharamblur.models.ModelDownloadManager
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.flow.StateFlow

class ImageModerationProcessor(private val context: Context) {

    private val faceDetector = FaceDetector()

    // Expose model download manager so presentation layer can observe state
    val modelDownloadManager = ModelDownloadManager(context)

    // Expose download state for convenience
    val downloadState: StateFlow<ModelDownloadManager.ModelDownloadState>
        get() = modelDownloadManager.downloadState

    private var genderModel: GenderDetectionModel? = null
    private var contentModel: ContentDetectionModel? = null
    private var modelsInitialized = false

    private val mutex = Mutex()
    private var activeJob: Job? = null



    // Make this public so presentation layer can trigger download
    suspend fun downloadModels(wifiOnly: Boolean = true) {
        modelDownloadManager.downloadModelsIfNeeded(wifiOnly)
    }

    // Check if models are ready without triggering download
    fun areModelsReady(): Boolean = modelDownloadManager.areModelsReady()

    // Initialize models asynchronously
    private suspend fun ensureModelsLoaded() {
        if (modelsInitialized) return

        mutex.withLock {
            if (modelsInitialized) return

            try {
                // Try to use downloaded models
                val modelFiles = modelDownloadManager.downloadModelsIfNeeded()
                genderModel = GenderDetectionModel(modelFiles.genderModelFile)
                contentModel = ContentDetectionModel(modelFiles.nsfwModelFile)
                modelsInitialized = true
            } catch (e: Exception) {
                Log.e("ImageModerationProcessor", "Failed to download models, using local assets", e)
                // Fallback to local assets
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
                if (useContentDetection && contentModel != null) {
                    contentModel!!.detectContent(bitmap)
                } else null
            }

            val facesDeferred = async {
                faceDetector.detectFaces(bitmap)
            }

            val contentResult = contentDeferred.await()
            val faces = facesDeferred.await()
            val faceInfoList = mutableListOf<FaceInfo>()

            if (contentResult != null && contentResult.isInappropriate) {
                return@withContext ProcessingResult(
                    shouldModerate = true,
                    reason = "Inappropriate content detected",
                    details = DetectionDetails(
                        contentScore = contentResult.score,
                        isInappropriate = true,
                        faceRegions = faceInfoList
                    )
                )
            }

            var femaleCount = 0
            var maleCount = 0
            var uncertainCount = 0

            for (face in faces) {
                val faceBitmap = cropFace(bitmap, face)
                val genderResult = genderModel?.detectGender(faceBitmap)

                if (genderResult != null) {
                    val gender = when {
                        genderResult.confidence < DEFAULT_GENDER_CONFIDENCE_THRESHOLD -> {
                            uncertainCount++
                            if (strictMode) femaleCount++
                            Gender.UNCERTAIN
                        }
                        genderResult.isFemale -> {
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
                            confidence = genderResult.confidence
                        )
                    )
                }
            }

            val shouldModerate = when {
                detectFemales && femaleCount > 0 -> true
                detectMales && maleCount > 0 -> true
                strictMode && uncertainCount > 0 -> true
                else -> false
            }

            val reason = when {
                shouldModerate && femaleCount > 0 -> "Detected $femaleCount female face(s)"
                shouldModerate && maleCount > 0 -> "Detected $maleCount male face(s)"
                shouldModerate && strictMode -> "Uncertain detection in strict mode"
                else -> null
            }

            ProcessingResult(
                shouldModerate = shouldModerate,
                reason = reason,
                details = DetectionDetails(
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

    private fun cropFace(bitmap: Bitmap, face: FaceDetector.DetectedFace): Bitmap {
        val rect = face.boundingBox
        val padding = (rect.width() * FACE_CROP_PADDING).toInt()

        val left = (rect.left - padding).coerceAtLeast(0)
        val top = (rect.top - padding).coerceAtLeast(0)
        val right = (rect.right + padding).coerceAtMost(bitmap.width)
        val bottom = (rect.bottom + padding).coerceAtMost(bitmap.height)

        val width = right - left
        val height = bottom - top

        return Bitmap.createBitmap(bitmap, left, top, width, height)
    }

    suspend fun cleanup() {
        Log.d("ImageModerationProcessor", "cleanup called, waiting for active moderation to complete")
        mutex.withLock {
            activeJob?.cancelAndJoin()
        }
        faceDetector.close()
        genderModel?.close()
        contentModel?.close()
        Log.d("ImageModerationProcessor", "cleanup completed")
    }

    companion object {
        const val DEFAULT_CONTENT_THRESHOLD = 0.3f
        private const val DEFAULT_GENDER_CONFIDENCE_THRESHOLD = 0.5f
        private const val FACE_CROP_PADDING = 0.15f
    }

    data class ProcessingResult(
        val shouldModerate: Boolean,
        val reason: String? = null,
        val details: DetectionDetails? = null
    )

    data class DetectionDetails(
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
}