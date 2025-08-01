package com.london.imageharamblur.ui

import android.graphics.Bitmap

data class ImageModerationState(
    val isProcessing: Boolean = false,
    val isModerated: Boolean = false,
    val shouldBlur: Boolean = false,
    val originalBitmap: Bitmap? = null,
    val blurredBitmap: Bitmap? = null,
    val error: String? = null
)