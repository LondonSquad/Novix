package com.london.imageharamblur.ui

import android.graphics.Bitmap
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp

private val isAndroid12OrAbove = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

@Composable
fun ModeratedImage(
    state: ImageModerationState,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
    blurStrength: Float = 80f
) {
    if (!state.isModerated || state.originalBitmap == null) return

    when {
        state.shouldBlur -> BlurredImage(
            state = state,
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = contentScale,
            blurStrength = blurStrength
        )
        else -> NormalImage(
            bitmap = state.originalBitmap,
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = contentScale
        )
    }
}

@Composable
private fun BlurredImage(
    state: ImageModerationState,
    contentDescription: String?,
    modifier: Modifier,
    contentScale: ContentScale,
    blurStrength: Float
) {
    when {
        isAndroid12OrAbove -> NativeBlurImage(
            bitmap = state.originalBitmap!!,
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = contentScale,
            blurStrength = blurStrength
        )
        else -> PreProcessedBlurImage(
            bitmap = state.blurredBitmap ?: state.originalBitmap!!,
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = contentScale
        )
    }
}

@Composable
private fun NativeBlurImage(
    bitmap: Bitmap,
    contentDescription: String?,
    modifier: Modifier,
    contentScale: ContentScale,
    blurStrength: Float
) {
    Image(
        bitmap = bitmap.asImageBitmap(),
        contentDescription = contentDescription,
        contentScale = contentScale,
        modifier = modifier
            .fillMaxSize()
            .blur(radius = blurStrength.dp)
    )
}

@Composable
private fun PreProcessedBlurImage(
    bitmap: Bitmap,
    contentDescription: String?,
    modifier: Modifier,
    contentScale: ContentScale
) {
    Image(
        bitmap = bitmap.asImageBitmap(),
        contentDescription = contentDescription,
        contentScale = contentScale,
        modifier = modifier.fillMaxSize()
    )
}

@Composable
private fun NormalImage(
    bitmap: Bitmap,
    contentDescription: String?,
    modifier: Modifier,
    contentScale: ContentScale
) {
    Image(
        bitmap = bitmap.asImageBitmap(),
        contentDescription = contentDescription,
        contentScale = contentScale,
        modifier = modifier.fillMaxSize()
    )
}