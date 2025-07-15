package com.ae.imageharamblur.ui

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        when {
            state.isModerated && state.originalBitmap != null -> {
                if (state.shouldBlur) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        Image(
                            bitmap = state.originalBitmap.asImageBitmap(),
                            contentDescription = contentDescription,
                            contentScale = contentScale,
                            modifier = Modifier
                                .fillMaxSize()
                                .blur(radius = blurStrength.dp)
                        )
                    } else {
                        state.blurredBitmap?.let { blurredBitmap ->
                            Image(
                                bitmap = blurredBitmap.asImageBitmap(),
                                contentDescription = contentDescription,
                                contentScale = contentScale,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                } else {
                    Image(
                        bitmap = state.originalBitmap.asImageBitmap(),
                        contentDescription = contentDescription,
                        contentScale = contentScale,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}