package com.london.designsystem.snackbar

import androidx.compose.runtime.staticCompositionLocalOf

val LocalSnackbarController = staticCompositionLocalOf<SnackbarController> {
    error("No SnackbarController provided.")
}
