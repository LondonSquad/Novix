package com.london.designsystem.snackbar

interface SnackbarController {
    fun showMessage(message: String, icon: Int?, snackbarType: SnackbarType)
}

enum class SnackbarType {
    Success,
    Error;
}

