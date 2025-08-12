package com.london.presentation.feature.authentication.login

sealed class LoginEffect {
    data object NavigateBack : LoginEffect()
    data object NavigateToHome : LoginEffect()
    data object NavigateToRegistration : LoginEffect()
    data class NavigateToCreateAccount(val url: String) : LoginEffect()
    data class NavigateToForgotPassword(val url: String) : LoginEffect()
}
