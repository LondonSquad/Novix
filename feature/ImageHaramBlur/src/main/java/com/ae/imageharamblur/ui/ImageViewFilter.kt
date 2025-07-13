package com.ae.imageharamblur.ui

import android.R
import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageScope
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.ae.imageharamblur.ImageModerationProcessor
import com.ae.imageharamblur.utils.blurBitmap
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
    blurStrength: Float = 80f,
    loadingContent: @Composable SubcomposeAsyncImageScope.(AsyncImagePainter.State.Loading) -> Unit,
    errorContent: @Composable SubcomposeAsyncImageScope.(AsyncImagePainter.State.Error) -> Unit,
    onModerationResult: ((Boolean, String?) -> Unit)? = null,
    onLoadingStateChange: ((Boolean) -> Unit)? = null
) {
    if (LocalInspectionMode.current) return PreviewImage()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var shouldBlur by remember(model) { mutableStateOf(false) }
    var isProcessing by remember(model) { mutableStateOf(false) }
    var blurredBitmap by remember(model) { mutableStateOf<Bitmap?>(null) }
    var showImage by remember(model) { mutableStateOf(false) }

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
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(context)
                .data(model)
                .crossfade(false)
                .allowHardware(false)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .diskCachePolicy(CachePolicy.ENABLED)
                .build(),
            contentDescription = contentDescription,
            contentScale = contentScale,
            loading = loadingContent,
            error = errorContent,
            onLoading = { state: AsyncImagePainter.State.Loading ->
                onLoadingStateChange?.invoke(true)
            },
            onSuccess = { state: AsyncImagePainter.State.Success ->

                if (enableModeration && processor != null && !isProcessing) {
                    isProcessing = true
                    onLoadingStateChange?.invoke(true)
                    scope.launch {
                        try {
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

                                    if (shouldBlur && Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                                        blurredBitmap = withContext(Dispatchers.Default) {
                                            blurBitmap(bitmap, blurStrength.toInt())
                                        }
                                    }

                                    onModerationResult?.invoke(
                                        processingResult.shouldModerate,
                                        processingResult.reason
                                    )
                                }
                            }
                        } catch (e: Exception) {
                            Log.e("ImageViewFilter", "Error during moderation", e)
                        } finally {
                            isProcessing = false
                            onLoadingStateChange?.invoke(false)
                            showImage = true

                        }
                    }
                } else {
                    onLoadingStateChange?.invoke(false)
                }
            },
            onError = {
                onLoadingStateChange?.invoke(false)
            },
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (shouldBlur) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            Modifier.blur(radius = blurStrength.dp)
                        } else Modifier
                    } else Modifier
                )
        )

        if (shouldBlur && Build.VERSION.SDK_INT < Build.VERSION_CODES.S && blurredBitmap != null) {
            Image(
                bitmap = blurredBitmap!!.asImageBitmap(),
                contentDescription = contentDescription,
                contentScale = contentScale,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun PreviewImage() {
    Image(
        painter = painterResource(R.drawable.star_on),
        contentDescription = "Preview Image",
        contentScale = ContentScale.FillBounds,
        modifier = Modifier.size(64.dp)
    )
}