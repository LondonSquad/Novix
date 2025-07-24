package com.london.presentation.screen.login

import androidx.compose.ui.text.input.TextFieldValue
import com.london.domain.usecase.login.LoginAsGuestUseCase
import com.london.domain.usecase.login.LoginUseCase
import com.london.presentation.screen.base.BaseViewModel
import com.london.presentation.screen.base.ErrorState
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val loginAsGuestUseCase: LoginAsGuestUseCase,
) : BaseViewModel<LoginUiState, LoginEffect>(LoginUiState()),
    LoginContract {

    companion object {
        private const val CREATE_ACCOUNT_URL = "https://www.themoviedb.org/signup"
        private const val FORGOT_PASSWORD_URL = "https://www.themoviedb.org/reset-password"
    }

    override fun onUsernameChanged(username: TextFieldValue) {
        updateState {
            copy(
                username = username,
                isLoginEnabled = username.toString().isNotEmpty() && password.text.isNotEmpty(),
                error = null
            )
        }
    }

    override fun onPasswordChanged(password: TextFieldValue) {
        updateState {
            copy(
                password = password,
                isLoginEnabled = username.text.isNotEmpty() && password.toString().isNotEmpty(),
                error = null
            )
        }
    }

    override fun onPasswordVisibilityToggled() {
        updateState {
            copy(passwordVisible = !passwordVisible)
        }
    }

    override fun onLoginClick() {
        val currentState = state.value
        if (currentState.username.text.isEmpty() || currentState.password.text.isEmpty()) {
            return
        }
        tryToExecute(
            block = { loginUseCase.invoke(currentState.username.text, currentState.password.text) },
            onStart = { updateState { copy(isLoading = true, error = null) } },
            onSuccess = { isSuccess: Boolean ->
                if (isSuccess) {
                    emitEffect(LoginEffect.NavigateToHome)
                } else {
                    updateState { copy(error = ErrorState.RequestFailed("Login failed. Please check your credentials.")) }
                }
            },
            onError = { errorState ->
                updateState { copy(error = errorState) }
            },
            onCompleted = {
                updateState { copy(isLoading = false) }
            }
        )
    }

    override fun onLoginAsGuestClick() {
        tryToExecute(
            block = { loginAsGuestUseCase.invoke() },
            onStart = { updateState { copy(isGuestLoginLoading = true, error = null) } },
            onSuccess = { isSuccess: Boolean ->
                if (isSuccess) {
                    emitEffect(LoginEffect.NavigateToHome)
                } else {
                    updateState { copy(error = ErrorState.RequestFailed("Guest login failed.")) }
                }
            },
            onError = { errorState ->
                updateState { copy(error = errorState) }
            },
            onCompleted = {
                updateState { copy(isGuestLoginLoading = false) }
            }
        )
    }

    override fun onNavigateBack() {
        emitEffect(LoginEffect.NavigateBack)
    }

    override fun onCreateAccountClick() {
        emitEffect(LoginEffect.NavigateToCreateAccount(CREATE_ACCOUNT_URL))
    }

    override fun onForgotPasswordClick() {
        emitEffect(LoginEffect.NavigateToForgotPassword(FORGOT_PASSWORD_URL))
    }
}