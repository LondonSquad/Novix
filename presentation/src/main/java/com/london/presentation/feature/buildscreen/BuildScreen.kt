package com.london.presentation.feature.buildscreen

import androidx.compose.runtime.Composable

@Composable
fun BuildScreen(
    isLoading: Boolean = false,
    isError: Boolean = false,
    content: @Composable () -> Unit
) {
    when{
        isLoading -> LoadingScreen()
        isError -> NetworkErrorScreen()
        else -> content()
    }
}