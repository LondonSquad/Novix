package com.london.presentation.screen

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