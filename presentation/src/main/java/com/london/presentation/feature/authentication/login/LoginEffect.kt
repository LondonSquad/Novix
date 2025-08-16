package com.london.presentation.feature.authentication.login

sealed class LoginEffect {
    data object NavigateBack : LoginEffect()
    data object NavigateToHome : LoginEffect()
    data object NavigateToRegistration : LoginEffect()
    data class NavigateToCreateAccount(val url: String) : LoginEffect()
    class NavigateToForgotPassword(val url: String = FORGOT_PASSWORD_URL) : LoginEffect() {
        private companion object {
            const val FORGOT_PASSWORD_URL = "https://www.themoviedb.org/reset-password"
        }
    }
}
