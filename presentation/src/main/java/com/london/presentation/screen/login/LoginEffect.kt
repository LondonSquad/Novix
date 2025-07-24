package com.london.presentation.screen.login

sealed class LoginEffect {
    data object NavigateToHome : LoginEffect()
    data object NavigateBack : LoginEffect()
    data class NavigateToCreateAccount(val url: String) : LoginEffect()
    data class NavigateToForgotPassword(val url: String) : LoginEffect()
}
