package com.london.presentation.feature.authentication.login

sealed class LoginEffect {
    data object BackNavigation : LoginEffect()
    data object HomeNavigation : LoginEffect()
    data object RegistrationNavigation : LoginEffect()
    data class CreateAccountNavigation(val url: String) : LoginEffect()
    class NavigateToForgotPassword(val url: String = FORGOT_PASSWORD_URL) : LoginEffect() {
        private companion object {
            const val FORGOT_PASSWORD_URL = "https://www.themoviedb.org/reset-password"
        }
    }
}
