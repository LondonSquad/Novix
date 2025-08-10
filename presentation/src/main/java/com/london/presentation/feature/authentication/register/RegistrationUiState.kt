package com.london.presentation.feature.authentication.register

import com.london.presentation.feature.authentication.register.Register.REGISTER_URL

data class RegistrationUiState(
    val registrationUrl: String = REGISTER_URL,
    val isLoading: Boolean = false,
    val currentUrl: String = ""
)

private object Register {
    const val REGISTER_URL = "https://www.themoviedb.org/signup"
}