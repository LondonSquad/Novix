package com.ae.imageharamblur.ui

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.ae.imageharamblur.ImageModerationProcessor
import com.ae.imageharamblur.utils.toBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ImageViewFilter(
    model: Any?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
    enableModeration: Boolean = true,
    blurStrength: Float = 20f,
    placeholder: Painter? = null,
    error: Painter? = null,
    onLoading: ((AsyncImagePainter.State.Loading) -> Unit)? = null,
    onSuccess: ((AsyncImagePainter.State.Success) -> Unit)? = null,
    onError: ((AsyncImagePainter.State.Error) -> Unit)? = null,
    onModerationResult: ((Boolean, String?) -> Unit)? = null
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var shouldBlur by remember(model) { mutableStateOf(false) }
    var isProcessing by remember(model) { mutableStateOf(false) }
    var showImage by remember(model) { mutableStateOf(!enableModeration) }

    val processor = remember(enableModeration) {
        if (enableModeration) {
            ImageModerationProcessor(context)
        } else null
    }

    DisposableEffect(processor) {
        onDispose {
            scope.launch {
                processor?.cleanup()
            }
        }
    }


    Box(modifier = modifier) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(model)
                .crossfade(true)
                .allowHardware(false)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .diskCachePolicy(CachePolicy.ENABLED)
                .build(),
            contentDescription = contentDescription,
            contentScale = contentScale,
            placeholder = placeholder,
            error = error,
            onLoading = onLoading,
            onSuccess = { state ->
                onSuccess?.invoke(state)

                if (enableModeration && processor != null && !isProcessing) {
                    isProcessing = true
                    scope.launch {
                        try {
                            // Add a check for drawable validity
                            val drawable = state.result.drawable
                            if (drawable.intrinsicWidth > 0 && drawable.intrinsicHeight > 0) {
                                val bitmap = drawable.toBitmap()
                                if (bitmap != null && !bitmap.isRecycled) {
                                    val processingResult = withContext(Dispatchers.Default) {
                                        processor.processImage(
                                            bitmap = bitmap,
                                            detectFemales = true,
                                            detectMales = false,
                                            useContentDetection = true,
                                            strictMode = false
                                        )
                                    }
                                    shouldBlur = processingResult.shouldModerate
                                    showImage = true
                                    onModerationResult?.invoke(
                                        processingResult.shouldModerate,
                                        processingResult.reason
                                    )
                                } else {
                                    showImage = true
                                }
                            } else {
                                showImage = true
                            }
                        } catch (e: Exception) {
                            Log.e("Moderation", "Failed during moderation", e)
                        } finally {
                            isProcessing = false
                        }
                    }
                } else {
                    showImage = true
                }
            },
            onError = { state ->
                onError?.invoke(state)
                showImage = true // Ensure image shows even on error
            },
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (shouldBlur) Modifier.blur(radius = blurStrength.dp) else Modifier
                ),
            alpha = if (showImage) 1f else 0f
        )
    }
}