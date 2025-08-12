package com.london.presentation.feature.authentication.register

import com.london.presentation.feature.authentication.register.Register.REGISTER_URL

data class RegistrationUiState(
    val currentUrl: String = "",
    val isLoading: Boolean = false,
    val registrationUrl: String = REGISTER_URL
)

private object Register {
    const val REGISTER_URL = "https://www.themoviedb.org/signup"
}