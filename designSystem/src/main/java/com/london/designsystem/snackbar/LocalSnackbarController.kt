package com.london.designsystem.snackbar

import androidx.compose.runtime.staticCompositionLocalOf

val LocalSnackbarController = staticCompositionLocalOf<SnackBarController> {
    error("No SnackbarController provided.")
}
