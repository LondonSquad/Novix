package com.london.presentation.feature.login

import androidx.compose.ui.text.input.TextFieldValue
import com.london.presentation.feature.base.ErrorState

data class LoginUiState(
    val isLoading: Boolean = false,
    val isGuestLoginLoading: Boolean = false,
    val username: TextFieldValue = TextFieldValue(""),
    val password: TextFieldValue = TextFieldValue(""),
    val passwordVisible: Boolean = false,
    val error: ErrorState? = null,
    val isLoginEnabled: Boolean = false
)
