package com.ae.imageharamblur

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.util.Log
import com.ae.imageharamblur.faceDetection.FaceDetector
import com.ae.imageharamblur.models.ContentDetectionModel
import com.ae.imageharamblur.models.GenderDetectionModel
import com.ae.imageharamblur.models.ModelDownloadManager
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.flow.StateFlow
import com.ae.imageharamblur.faceDetection.DetectedFace

class ImageModerationProcessor(private val context: Context) {

    companion object {
        private const val TAG = "ImageModeration"
        const val DEFAULT_CONTENT_THRESHOLD = 0.3f
        private const val DEFAULT_GENDER_CONFIDENCE_THRESHOLD = 0.5f
        private const val FACE_CROP_PADDING = 0.15f
    }

    private val faceDetector = FaceDetector(context)
    val modelDownloadManager = ModelDownloadManager(context)
    val downloadState: StateFlow<ModelDownloadManager.ModelDownloadState>
        get() = modelDownloadManager.downloadState

    private var genderModel: GenderDetectionModel? = null
    private var contentModel: ContentDetectionModel? = null
    private var modelsInitialized = false

    private val mutex = Mutex()
    private var activeJob: Job? = null

    init {
        Log.d(TAG, "ImageModerationProcessor initialized")
    }

    suspend fun downloadModels(wifiOnly: Boolean = true) {
        Log.d(TAG, "downloadModels called with wifiOnly=$wifiOnly")
        modelDownloadManager.downloadModelsIfNeeded(wifiOnly)
    }

    fun areModelsReady(): Boolean {
        val ready = modelDownloadManager.areModelsReady()
        Log.d(TAG, "areModelsReady: $ready")
        return ready
    }

    private suspend fun ensureModelsLoaded() {
        Log.d(TAG, "ensureModelsLoaded called, modelsInitialized=$modelsInitialized")

        if (modelsInitialized) return

        mutex.withLock {
            if (modelsInitialized) return

            try {
                Log.d(TAG, "Loading models...")
                val modelFiles = modelDownloadManager.downloadModelsIfNeeded()

                Log.d(TAG, "Initializing GenderDetectionModel with file: ${modelFiles.genderModelFile.absolutePath}")
                genderModel = GenderDetectionModel(modelFiles.genderModelFile)

                Log.d(TAG, "Initializing ContentDetectionModel with file: ${modelFiles.nsfwModelFile.absolutePath}")
                contentModel = ContentDetectionModel(modelFiles.nsfwModelFile)

                modelsInitialized = true
                Log.d(TAG, "Models loaded successfully")
            } catch (e: Exception) {
                Log.e(TAG, "Failed to download models, using local assets", e)
                genderModel = GenderDetectionModel(context)
                contentModel = ContentDetectionModel(context)
                modelsInitialized = true
                Log.d(TAG, "Fallback to local assets completed")
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

        Log.d(TAG, "=== processImage START ===")
        Log.d(TAG, "Bitmap dimensions: ${bitmap.width}x${bitmap.height}")
        Log.d(TAG, "Bitmap config: ${bitmap.config}")
        Log.d(TAG, "Parameters: detectFemales=$detectFemales, detectMales=$detectMales, useContentDetection=$useContentDetection, strictMode=$strictMode")

        ensureModelsLoaded()

        mutex.withLock {
            activeJob = coroutineContext[Job]
        }

        try {
            // Content detection
            val contentDeferred = async {
                if (useContentDetection && contentModel != null) {
                    Log.d(TAG, "Starting content detection...")
                    val result = contentModel!!.detectContent(bitmap)
                    Log.d(TAG, "Content detection result: isInappropriate=${result.isInappropriate}, score=${result.score}")
                    result
                } else {
                    Log.d(TAG, "Content detection skipped (useContentDetection=$useContentDetection, contentModel=${contentModel != null})")
                    null
                }
            }

            // Face detection
            val facesDeferred = async {
                Log.d(TAG, "Starting face detection...")
                val startTime = System.currentTimeMillis()
                val detectedFaces = faceDetector.detectFaces(bitmap)
                val duration = System.currentTimeMillis() - startTime

                Log.d(TAG, "Face detection completed in ${duration}ms")
                Log.d(TAG, "Number of faces detected: ${detectedFaces.size}")

                detectedFaces.forEachIndexed { index, face ->
                    Log.d(TAG, "Face $index: boundingBox=${face.boundingBox}, confidence=${face.confidence}")
                }

                detectedFaces
            }

            val contentResult = contentDeferred.await()
            val faces = facesDeferred.await()
            val faceInfoList = mutableListOf<FaceInfo>()

            // Check content first
            if (contentResult != null && contentResult.isInappropriate) {
                Log.d(TAG, "Content flagged as inappropriate, returning early")
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

            // Process faces for gender
            var femaleCount = 0
            var maleCount = 0
            var uncertainCount = 0

            Log.d(TAG, "Processing ${faces.size} faces for gender detection...")

            for ((index, face) in faces.withIndex()) {
                Log.d(TAG, "Processing face $index...")

                try {
                    val faceBitmap = cropFace(bitmap, face)
                    Log.d(TAG, "Face $index cropped: ${faceBitmap.width}x${faceBitmap.height}")

                    val genderResult = genderModel?.detectGender(faceBitmap)

                    if (genderResult != null) {
                        Log.d(TAG, "Face $index gender result: isFemale=${genderResult.isFemale}, confidence=${genderResult.confidence}")

                        val gender = when {
                            genderResult.confidence < DEFAULT_GENDER_CONFIDENCE_THRESHOLD -> {
                                uncertainCount++
                                if (strictMode) femaleCount++
                                Log.d(TAG, "Face $index classified as UNCERTAIN (low confidence)")
                                Gender.UNCERTAIN
                            }
                            genderResult.isFemale -> {
                                femaleCount++
                                Log.d(TAG, "Face $index classified as FEMALE")
                                Gender.FEMALE
                            }
                            else -> {
                                maleCount++
                                Log.d(TAG, "Face $index classified as MALE")
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
                    } else {
                        Log.w(TAG, "Face $index: gender detection returned null")
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error processing face $index", e)
                }
            }

            Log.d(TAG, "Gender detection summary: females=$femaleCount, males=$maleCount, uncertain=$uncertainCount")

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

            Log.d(TAG, "Final decision: shouldModerate=$shouldModerate, reason=$reason")

            val result = ProcessingResult(
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

            Log.d(TAG, "=== processImage END ===")
            return@withContext result

        } catch (e: Exception) {
            Log.e(TAG, "Error in processImage", e)
            throw e
        } finally {
            mutex.withLock {
                activeJob = null
            }
        }
    }

    private fun cropFace(bitmap: Bitmap, face: DetectedFace): Bitmap {
        val rect = face.boundingBox
        val padding = (rect.width() * FACE_CROP_PADDING).toInt()

        Log.d(TAG, "Cropping face: original rect=$rect, padding=$padding")

        val left = (rect.left - padding).coerceAtLeast(0)
        val top = (rect.top - padding).coerceAtLeast(0)
        val right = (rect.right + padding).coerceAtMost(bitmap.width)
        val bottom = (rect.bottom + padding).coerceAtMost(bitmap.height)

        val width = right - left
        val height = bottom - top

        Log.d(TAG, "Crop bounds: left=$left, top=$top, width=$width, height=$height")

        return Bitmap.createBitmap(bitmap, left, top, width, height)
    }

    suspend fun cleanup() {
        Log.d(TAG, "cleanup called, waiting for active moderation to complete")
        mutex.withLock {
            activeJob?.cancelAndJoin()
        }
        faceDetector.close()
        genderModel?.close()
        contentModel?.close()
        Log.d(TAG, "cleanup completed")
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