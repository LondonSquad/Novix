package com.ae.imageharamblur

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import com.ae.imageharamblur.detection.FaceDetector
import com.ae.imageharamblur.models.ContentDetectionModel
import com.ae.imageharamblur.models.GenderDetectionModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class ImageModerationProcessor(private val context: Context) {
    private val faceDetector = FaceDetector()
    private val genderModel by lazy { GenderDetectionModel(context) }
    private val contentModel by lazy { ContentDetectionModel(context) }

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

    suspend fun processImage(
        bitmap: Bitmap,
        detectFemales: Boolean = true,
        detectMales: Boolean = false,
        useContentDetection: Boolean = true,
        strictMode: Boolean = false
    ): ProcessingResult = withContext(Dispatchers.Default) {
        val contentDeferred = async {
            if (useContentDetection) {
                contentModel.detectContent(bitmap)
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
            val genderResult = genderModel.detectGender(faceBitmap)

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

    fun cleanup() {
        faceDetector.close()
        genderModel.close()
        contentModel.close()
    }
}