package com.london.imageharamblur.ui

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.Coil
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.london.imageharamblur.extensions.toBitmap
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

    val controller = remember(context, imageKey, config) {
        if (config.enableModeration) {
            createModerationController(
                context = context,
                imageKey = imageKey,
                config = config
            )
        } else null
    }

    DisposableEffect(controller) {
        onDispose {
            controller?.close()
        }
    }

    LaunchedEffect(model, config) {
        onLoadingStateChange?.invoke(true)
        isLoading = true
        errorState = null
        moderationState = null

        try {
            val drawable = loadImageDrawable(context, model)

            if (drawable == null) {
                errorState = "Failed to load image"
                isLoading = false
                onLoadingStateChange?.invoke(false)
                return@LaunchedEffect
            }

            if (!config.enableModeration || controller == null) {
                val bitmap = drawable.toBitmap()
                moderationState = ImageModerationState(
                    isProcessing = false,
                    isModerated = true,
                    shouldBlur = false,
                    originalBitmap = bitmap
                )
                onModerationResult?.invoke(false)
                isLoading = false
                onLoadingStateChange?.invoke(false)
                return@LaunchedEffect
            }

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
            errorState = e.message ?: "Unknown error"
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
                if (moderationState!!.shouldBlur && config.showCustomContentWhenBlurred) {
                    moderatedContent()
                } else {
                    // Show unblurred image if showTextInsteadOfBlur is true
                    val shouldActuallyBlur = moderationState!!.shouldBlur && !config.showTextInsteadOfBlur

                    ModeratedImage(
                        state = moderationState!!.copy(shouldBlur = shouldActuallyBlur),
                        contentDescription = contentDescription,
                        contentScale = contentScale,
                        blurStrength = config.blurStrength,
                        modifier = Modifier.fillMaxSize()
                    )
                     if (moderationState!!.shouldBlur){
                         moderatedContent()
                     }

                    // Show text overlay when image should be blurred but showTextInsteadOfBlur is true
                    if (moderationState!!.shouldBlur && config.showTextInsteadOfBlur) {
                        Box(
                            modifier = Modifier
                                .background(
                                    color = Color.Red.copy(alpha = 0.7f),
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            BasicText(
                                text = "Image must be blurred",
                                style = TextStyle(
                                    color = Color.White,
                                    fontSize = 14.sp
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

data class ImageFilterConfig(
    val enableModeration: Boolean = true,
    val blurStrength: Float = 80f,
    val detectFemales: Boolean = true,
    val detectMales: Boolean = false,
    val useContentDetection: Boolean = true,
    val showCustomContentWhenBlurred: Boolean = false,
    val showTextInsteadOfBlur: Boolean = false
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
        val result = Coil.imageLoader(context).execute(request)
        result.drawable
    } catch (_: Exception) {
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