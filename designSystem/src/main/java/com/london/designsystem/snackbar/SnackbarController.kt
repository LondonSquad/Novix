package com.london.designsystem.snackbar

interface SnackbarController {
    fun showMessage(
        message: String,
        icon: Int?,
        snackbarType: SnackbarType,
        onComplete: () -> Unit
    )
}

enum class SnackbarType {
    Success,
    Error;
}
