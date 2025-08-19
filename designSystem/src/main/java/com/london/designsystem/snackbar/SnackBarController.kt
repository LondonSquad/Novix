package com.london.designsystem.snackbar

interface SnackBarController {
    fun showMessage(
        message: String,
        icon: Int?,
        snackBarType: SnackBarType,
        onComplete: () -> Unit
    )
}

enum class SnackBarType {
    Success,
    Error;
}
