package com.ae.imageharamblur.ui

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

    val processor = remember(enableModeration) {
        if (enableModeration) {
            ImageModerationProcessor(context)
        } else null
    }

    DisposableEffect(processor) {
        onDispose {
            processor?.cleanup()
        }
    }

    Box(modifier = modifier) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(model)
                .crossfade(true)
                .allowHardware(false)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .listener(
                    onSuccess = { _, result ->
                        if (enableModeration && processor != null && !isProcessing) {
                            isProcessing = true
                            scope.launch {
                                try {
                                    result.drawable.toBitmap()?.let { bitmap ->
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
                                        onModerationResult?.invoke(
                                            processingResult.shouldModerate,
                                            processingResult.reason
                                        )
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                } finally {
                                    isProcessing = false
                                }
                            }
                        }
                    }
                )
                .build(),
            contentDescription = contentDescription,
            contentScale = contentScale,
            placeholder = placeholder,
            error = error,
            onLoading = onLoading,
            onSuccess = onSuccess,
            onError = onError,
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (shouldBlur) {
                        Modifier.blur(radius = blurStrength.dp)
                    } else {
                        Modifier
                    }
                )
        )
    }
}