package com.london.presentation.utils

import androidx.compose.ui.platform.UriHandler
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun UriHandler.openUrl(url: String) = runCatching {
    openUri(url)
}

fun ViewModel.launchCatching(block: suspend () -> Unit) = viewModelScope.launch(Dispatchers.IO) {
    runCatching { block() }
}