package com.london.imageharamblur.ui

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.Coil
import coil.request.CachePolicy
import coil.request.ImageRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun ImageViewFilter(
    model: Any?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
    config: ImageFilterConfig = ImageFilterConfig(),
    loadingContent: @Composable () -> Unit,
    errorContent: @Composable (String?) -> Unit,
    onModerationResult: ((Boolean) -> Unit)? = null,
    onLoadingStateChange: ((Boolean) -> Unit)? = null,
    moderatedContent: @Composable () -> Unit = @Composable {}
) {
    val context = LocalContext.current
    val imageKey = generateImageKey(model)

    var moderationState by remember { mutableStateOf<ImageModerationState?>(null) }
    var errorState by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(model, config.enableModeration) {
        onLoadingStateChange?.invoke(true)
        isLoading = true
        errorState = null

        var controller: ImageModerationController? = null

        try {
            val drawable = loadImageDrawable(context, model)

            if (drawable == null) {
                errorState = "Failed to load image for moderation."
                isLoading = false
                onLoadingStateChange?.invoke(false)
                return@LaunchedEffect
            }

            controller = createModerationController(
                context = context,
                imageKey = imageKey,
                config = config
            )

            val state = controller.processImage(
                drawable = drawable,
                detectFemales = config.detectFemales,
                detectMales = config.detectMales,
                useContentDetection = config.useContentDetection
            )

            moderationState = state
            onModerationResult?.invoke(state.shouldBlur)
            isLoading = false
            onLoadingStateChange?.invoke(false)
        } catch (e: Exception) {
            errorState = e.message
            isLoading = false
            onLoadingStateChange?.invoke(false)
        } finally {
            controller?.close()
        }
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        when {
            isLoading -> loadingContent()
            errorState != null -> errorContent(errorState)
            moderationState != null && moderationState!!.isModerated -> {
                ModeratedImage(
                    state = moderationState!!,
                    contentDescription = contentDescription,
                    contentScale = contentScale,
                    blurStrength = config.blurStrength,
                    modifier = Modifier.fillMaxSize()
                )

                if (moderationState!!.shouldBlur) {
                    moderatedContent()
                }
            }
            else -> Log.d(
                "ImageViewFilter",
                "No content to show - moderationState: $moderationState"
            )
        }
    }
}

data class ImageFilterConfig(
    val enableModeration: Boolean = true,
    val blurStrength: Float = 80f,
    val detectFemales: Boolean = true,
    val detectMales: Boolean = false,
    val useContentDetection: Boolean = true
)

private fun generateImageKey(model: Any?): String = when (model) {
    is String -> model
    is Int -> model.toString()
    else -> model.hashCode().toString()
}

private suspend fun loadImageDrawable(
    context: Context,
    model: Any?
): Drawable? = withContext(Dispatchers.IO) {
    val request = ImageRequest.Builder(context)
        .data(model)
        .allowHardware(false)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .diskCachePolicy(CachePolicy.ENABLED)
        .build()

    try {
        Coil.imageLoader(context).execute(request).drawable
    } catch (e: Exception) {
        Log.e("ImageViewFilter", "Failed to load image", e)
        null
    }
}

private fun createModerationController(
    context: Context,
    imageKey: String,
    config: ImageFilterConfig
): ImageModerationController = ImageModerationController(
    context = context,
    cacheKey = imageKey,
    enableModeration = config.enableModeration,
    blurStrength = config.blurStrength
)