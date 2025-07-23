package com.london.presentation.screen.login

sealed class LoginEffect {
    object NavigateToHome : LoginEffect()
    object WebAuthProcessCompleted : LoginEffect()
    object NavigateBack : LoginEffect()
    data class NavigateToCreateAccount(val url: String) : LoginEffect()
    data class NavigateToForgotPassword(val url: String) : LoginEffect()
}