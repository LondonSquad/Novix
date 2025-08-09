package com.london.presentation.feature.authentication.login

import androidx.compose.ui.text.input.TextFieldValue

interface LoginContract {
    fun onLoginClick()
    fun onNavigateBack()

    fun onUsernameChanged(username: TextFieldValue)
    fun onPasswordChanged(password: TextFieldValue)
    fun onPasswordVisibilityToggled()

    fun onLoginAsGuestClick()
    fun onCreateAccountClick()
    fun onForgotPasswordClick()
}