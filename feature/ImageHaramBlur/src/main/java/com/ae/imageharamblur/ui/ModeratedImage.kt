package com.ae.imageharamblur.ui

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp

@Composable
fun ModeratedImage(
    state: ImageModerationState,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
    blurStrength: Float = 80f
) {
    if (!state.isModerated || state.originalBitmap == null) return

    val imageModifier = if (state.shouldBlur && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        Modifier
            .fillMaxSize()
            .blur(radius = blurStrength.dp)
    } else {
        Modifier.fillMaxSize()
    }

    val bitmapToShow = if (state.shouldBlur && Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
        state.blurredBitmap ?: state.originalBitmap
    } else {
        state.originalBitmap
    }

    Image(
        bitmap = bitmapToShow.asImageBitmap(),
        contentDescription = contentDescription,
        contentScale = contentScale,
        modifier = modifier.then(imageModifier)
    )
}
