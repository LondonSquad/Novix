package com.london.presentation.feature.login.registrasion

data class WebViewRegistrationUiState(
    val registrationUrl: String = "https://www.themoviedb.org/signup",
    val isLoading: Boolean = false,
    val currentUrl: String = ""
)