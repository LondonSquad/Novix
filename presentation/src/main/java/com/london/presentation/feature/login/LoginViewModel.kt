package com.london.presentation.feature.login

import androidx.compose.ui.text.input.TextFieldValue
import com.london.domain.usecase.login.LoginAsGuestUseCase
import com.london.domain.usecase.login.LoginUseCase
import com.london.presentation.feature.base.BaseViewModel
import com.london.presentation.feature.base.ErrorState
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val loginAsGuestUseCase: LoginAsGuestUseCase,
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
        updateState {
            val trimmedPassword = username.copy(text = password.text.trim())
            copy(
                password = trimmedPassword,
                isLoginEnabled = username.text.isNotEmpty()
                        && password.text.isNotEmpty() && password.text.length >= 4,
                error = null
            )
        }
    }


    override fun onPasswordVisibilityToggled() {
        updateState {
            copy(passwordVisible = !passwordVisible)
        }
    }

    override fun onCreateAccountClick() {
        emitEffect(LoginEffect.NavigateToWebViewRegistration)
    }

    override fun onForgotPasswordClick() {
        emitEffect(LoginEffect.NavigateToForgotPassword(FORGOT_PASSWORD_URL))
    }

    override fun onLoginClick() {
        val currentState = state.value
        val username = currentState.username.text
        val password = currentState.password.text

        if (username.isEmpty() || password.isEmpty()) return

        tryToExecute(
            block = { loginUseCase.invoke(username, password) },
            onStart = { updateState { copy(isLoading = true, error = null) } },
            onSuccess = { isSuccess: Boolean ->
                if (isSuccess)
                    emitEffect(LoginEffect.NavigateToHome)
                else
                    updateState { copy(error = ErrorState.RequestFailed("Login failed. Please check your credentials.")) }

            },
            onError = {
                updateState { copy(error = ErrorState.RequestFailed("Login failed. Please check your credentials.")) }
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
            onError = {
                updateState { copy(error = ErrorState.RequestFailed("Guest login failed.")) }
            },
            onCompleted = {
                updateState { copy(isGuestLoginLoading = false) }
            }
        )
    }

    override fun onNavigateBack() {
        emitEffect(LoginEffect.NavigateBack)
    }

    companion object {
        private const val CREATE_ACCOUNT_URL = "https://www.themoviedb.org/signup"
        private const val FORGOT_PASSWORD_URL = "https://www.themoviedb.org/reset-password"
    }
}
