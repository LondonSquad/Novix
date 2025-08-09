package com.london.presentation.feature.authentication.register

data class WebViewRegistrationUiState(
    val registrationUrl: String = "https://www.themoviedb.org/signup",
    val isLoading: Boolean = false,
    val currentUrl: String = ""
)