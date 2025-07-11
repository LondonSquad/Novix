package com.london.presentation.utils

import androidx.compose.runtime.Composable

@Composable
fun <T> ResultOrEmpty(
    items: List<T>,
    otherItems: List<T>? = null,
    emptyContent: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    if (items.isEmpty() && otherItems.isNullOrEmpty()) {
        emptyContent()
    } else {
        content()
    }
}