package com.london.presentation.feature.authentication.login

import androidx.compose.ui.text.input.TextFieldValue
import com.london.presentation.shared.base.ErrorState

data class LoginUiState(
    val error: ErrorState? = null,
    val isLoading: Boolean = false,
    val isLoginEnabled: Boolean = false,
    val passwordVisible: Boolean = false,
    val isGuestLoginLoading: Boolean = false,
    val username: TextFieldValue = TextFieldValue(""),
    val password: TextFieldValue = TextFieldValue("")
)
