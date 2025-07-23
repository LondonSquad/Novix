package com.london.presentation.screen.login

import androidx.compose.ui.text.input.TextFieldValue

interface LoginContract {
 fun onUsernameChanged(username: TextFieldValue)
 fun onPasswordChanged(password: TextFieldValue)
 fun onPasswordVisibilityToggled()
 fun onLoginClick()
 fun onLoginAsGuestClick()

 // fun onWebAuthClick()
 fun onNavigateBack()
 fun onCreateAccountClick()
 fun onForgotPasswordClick()
}