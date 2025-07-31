package com.london.presentation.utils

import androidx.compose.ui.platform.UriHandler

fun UriHandler.openUrl(url: String) = runCatching {
    openUri(url)
}

fun String.trimExcessiveSpaces(): String {
    return this.replace(Regex("\\s+"), " ").trim()
}
