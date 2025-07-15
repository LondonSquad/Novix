package com.ae.imageharamblur.ui

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
    enableModeration: Boolean = true,
    blurStrength: Float = 60f,
    detectFemales: Boolean = true,
    detectMales: Boolean = false,
    useContentDetection: Boolean = true,
    strictMode: Boolean = false,
    loadingContent: @Composable () -> Unit,
    errorContent: @Composable (String?) -> Unit,
    onModerationResult: ((Boolean, String?) -> Unit)? = null,
    onLoadingStateChange: ((Boolean) -> Unit)? = null
) {
    val context = LocalContext.current

    val imageKey = remember(model) {
        when (model) {
            is String -> model
            is Int -> model.toString()
            else -> model.hashCode().toString()
        }
    }

    var moderationState by remember { mutableStateOf<ImageModerationState?>(null) }
    var errorState by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(model, enableModeration) {
        onLoadingStateChange?.invoke(true)
        isLoading = true
        errorState = null

        try {
            val request = ImageRequest.Builder(context)
                .data(model)
                .allowHardware(false)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .diskCachePolicy(CachePolicy.ENABLED)
                .build()

            val result = withContext(Dispatchers.IO) {
                Coil.imageLoader(context).execute(request)
            }

            val drawable = result.drawable
            if (drawable == null) {
                throw IllegalStateException("Failed to load image for moderation.")
            }

            val controller = ImageModerationController(
                context = context,
                cacheKey = imageKey,
                enableModeration = enableModeration,
                blurStrength = blurStrength
            )

            val state = controller.processImage(
                drawable = drawable,
                detectFemales = detectFemales,
                detectMales = detectMales,
                useContentDetection = useContentDetection,
                strictMode = strictMode
            )

            moderationState = state
            onModerationResult?.invoke(state.shouldBlur, state.moderationReason)
            isLoading = false
            onLoadingStateChange?.invoke(false)
        } catch (e: Exception) {
            errorState = e.message
            isLoading = false
            onLoadingStateChange?.invoke(false)
        }
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        when {
            isLoading -> {
                loadingContent()
            }

            errorState != null -> {
                errorContent(errorState)
            }

            moderationState != null && moderationState!!.isModerated -> {
                ModeratedImage(
                    state = moderationState!!,
                    contentDescription = contentDescription,
                    contentScale = contentScale,
                    blurStrength = blurStrength,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
