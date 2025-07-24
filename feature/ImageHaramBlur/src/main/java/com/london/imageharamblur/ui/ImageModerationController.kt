package com.london.imageharamblur.ui

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import com.london.imageharamblur.ImageModerationProcessor
import com.london.imageharamblur.utils.blurBitmap
import com.london.imageharamblur.utils.toBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext

class ImageModerationController(
    context: Context,
    private val cacheKey: String,
    private val enableModeration: Boolean = true,
    private val blurStrength: Float = 80f
) {
    private val processor = if (enableModeration) {
        ImageModerationProcessor(context)
    } else null

    private val _state = MutableStateFlow(
        ModerationCacheManager.get(cacheKey) ?: ImageModerationState()
    )
    val state: StateFlow<ImageModerationState> = _state

    suspend fun processImage(
        drawable: Drawable,
        detectFemales: Boolean = true,
        detectMales: Boolean = false,
        useContentDetection: Boolean = true
    ): ImageModerationState = withContext(Dispatchers.Default) {

        ModerationCacheManager.get(cacheKey)?.let { cachedState ->
            if (cachedState.isModerated && cachedState.originalBitmap != null) {
                _state.value = cachedState
                return@withContext cachedState
            }
        }

        try {
            _state.value = _state.value.copy(isProcessing = true, error = null)

            val bitmap = drawable.toBitmap()
            if (bitmap == null || bitmap.isRecycled) {
                val errorState = _state.value.copy(
                    isProcessing = false,
                    error = "Invalid bitmap"
                )
                _state.value = errorState
                return@withContext errorState
            }

            if (!enableModeration || processor == null) {
                val newState = ImageModerationState(
                    isProcessing = false,
                    isModerated = true,
                    shouldBlur = false,
                    originalBitmap = bitmap
                )
                _state.value = newState
                ModerationCacheManager.put(cacheKey, newState)
                return@withContext newState
            }

            val shouldModerate = processor.shouldModerateImage(
                bitmap = bitmap,
                detectFemales = detectFemales,
                detectMales = detectMales,
                useContentDetection = useContentDetection
            )

            val blurredBitmap = if (shouldModerate && Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                blurBitmap(bitmap, blurStrength.toInt())
            } else null

            val newState = ImageModerationState(
                isProcessing = false,
                isModerated = true,
                shouldBlur = shouldModerate,
                originalBitmap = bitmap,
                blurredBitmap = blurredBitmap
            )

            _state.value = newState
            ModerationCacheManager.put(cacheKey, newState)

            newState
        } catch (e: Exception) {
            val errorState = _state.value.copy(
                isProcessing = false,
                error = e.message
            )
            _state.value = errorState
            errorState
        }
    }

    fun close() {
        processor?.close()
    }
}