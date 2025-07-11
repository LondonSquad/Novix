package com.ae.imageharamblur.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
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
                .listener(
                    onSuccess = { _, result ->
                        if (enableModeration && processor != null && !isProcessing) {
                            isProcessing = true
                            scope.launch {
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
                                isProcessing = false
                            }
                        }
                    }
                )
                .build(),
            contentDescription = contentDescription,
            contentScale = contentScale,
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

        if (isProcessing) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(shimmerBrush())
            )
        }
    }
}

@Composable
private fun shimmerBrush(): Brush {
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.6f),
    )

    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer"
    )

    return Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim, y = translateAnim)
    )
}