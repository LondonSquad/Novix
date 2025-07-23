package com.london.presentation.screen.login

import androidx.compose.ui.text.input.TextFieldValue
import com.london.presentation.screen.base.ErrorState

// UI State
data class LoginUiState(
    val isLoading: Boolean = false,
    val isGuestLoginLoading: Boolean = false,
    val username: TextFieldValue = TextFieldValue(""),
    val password: TextFieldValue = TextFieldValue(""),
    val passwordVisible: Boolean = false,
    val error: ErrorState? = null,
    val isLoginEnabled: Boolean = false
)