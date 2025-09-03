package com.london.designsystem.snackbar

import androidx.annotation.DrawableRes
import com.london.designsystem.R

interface SnackBarController {
    fun showSnackBar(snackBarData: SnackBarData)
}

data class SnackBarData(
    val message: String,
    val snackBarType: SnackBarType,
    val snackbarDuration: SnackbarDuration = SnackbarDuration.Medium,
    val onComplete: () -> Unit = {},
)

enum class SnackBarType {
    Success,
    Error;

    @DrawableRes
    fun getDefaultIcon(): Int = when (this) {
        Success -> R.drawable.ic_success
        Error -> R.drawable.ic_failed
    }
}

enum class SnackbarDuration {
    Brief,
    Medium,
    Extensive,
    Indefinite;

    fun toMillis(): Long {
        return when (this) {
            Brief -> 1500
            Medium -> 3000
            Extensive -> 5000
            Indefinite -> Long.MAX_VALUE
        }
    }
}
