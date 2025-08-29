package com.london.designsystem.snackbar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf

val LocalSnackbarController = compositionLocalOf<SnackBarController> {
    error("No SnackBarController provided")
}

@Composable
fun rememberSnackBarController(): SnackBarController {
    return LocalSnackbarController.current
}
