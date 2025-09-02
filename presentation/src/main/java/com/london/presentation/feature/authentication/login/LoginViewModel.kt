package com.london.presentation.feature.authentication.login

import androidx.compose.ui.text.input.TextFieldValue
import com.london.domain.usecase.authentication.AuthenticationUseCase
import com.london.presentation.shared.base.BaseViewModel
import com.london.presentation.shared.base.ErrorState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authenticationUseCase: AuthenticationUseCase,
) : BaseViewModel<LoginUiState, LoginEffect>(LoginUiState()),
    LoginContract {

    override fun onUsernameChanged(username: TextFieldValue) {
        val trimmedUsername = username.copy(text = username.text.trim())
        updateState {
            copy(
                username = trimmedUsername,
                isLoginEnabled = username.text.isNotEmpty() && password.text.isNotEmpty(),
                error = null
            )
        }
    }

    override fun onPasswordChanged(password: TextFieldValue) {
        val currentUsername = state.value.username.text
        updateState {
            copy(
                password = password,
                isLoginEnabled = isLoginEnabled(currentUsername, password.text),
                error = null
            )
        }
    }

    override fun onPasswordVisibilityToggled() {
        updateState { copy(passwordVisible = !passwordVisible) }
    }

    override fun onCreateAccountClick() {
        emitEffect(LoginEffect.RegistrationNavigation)
    }

    override fun onForgotPasswordClick() {
        emitEffect(LoginEffect.NavigateToForgotPassword())
    }

    override fun onLoginClick() {
        val currentState = state.value
        val username = currentState.username.text
        val password = currentState.password.text

        if (username.isEmpty() || password.isEmpty()) return

        performLogin(username = username, password = password)
    }

    override fun onLoginAsGuestClick() {
        tryToExecute(
            block = { authenticationUseCase.loginAsGuest() },
            onStart = { updateState { copy(isGuestLoginLoading = true, error = null) } },
            onSuccess = { isSuccess -> checkLoginAsGuest(isSuccess) },
            onError = { handleLoginAsGuestError() },
            onCompleted = { updateState { copy(isGuestLoginLoading = false) } }
        )
    }

    override fun onNavigateBack() {
        emitEffect(LoginEffect.BackNavigation)
    }

    private fun handleLoginAsGuestError() {
        updateState { copy(error = ErrorState.RequestFailed()) }
    }

    private fun handleLoginError() {
        updateState { copy(error = ErrorState.RequestFailed()) }
    }

    private fun checkLoginAsGuest(isSuccess: Boolean) {
        when (isSuccess) {
            true -> emitEffect(LoginEffect.HomeNavigation)
            false -> handleLoginAsGuestError()
        }
    }

    private fun isLoginEnabled(username: String, password: String): Boolean {
        return username.isNotEmpty() && password.isNotEmpty() && password.length >= 4
    }

    private fun performLogin(username: String, password: String) {
        tryToExecute(
            block = { authenticationUseCase.login(username, password) },
            onStart = { updateState { copy(isLoading = true, error = null) } },
            onSuccess = { isSuccess: Boolean ->
                if (isSuccess)
                    emitEffect(LoginEffect.HomeNavigation)
                else
                    handleLoginError()
            },
            onError = { handleLoginError() },
            onCompleted = {
                updateState { copy(isLoading = false) }
            }
        )
    }
}
