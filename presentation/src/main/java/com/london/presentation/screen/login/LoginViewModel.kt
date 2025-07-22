package com.london.presentation.screen.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.london.domain.usecase.login.LoginAsGuestUseCase
import com.london.domain.usecase.login.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val loginAsGuestUseCase: LoginAsGuestUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state


    fun onUsernameChange(username: String) {
        _state.update { it.copy(username = username) }
    }

    fun onPasswordChange(password: String) {
        _state.update { it.copy(password = password) }
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }

    fun clearUrl() {
        _state.update { it.copy(launchUrl = null) }
    }

    //Bassent01
//    12345
    fun login() {
        val username = _state.value.username.trim()
        val password = _state.value.password.trim()

        if (username.isEmpty() || password.isEmpty()) {
            _state.update { it.copy(launchUrl = "https://www.themoviedb.org/signup") }
            return
        }

        viewModelScope.launch {
            Log.d("LoginViewModel", "Login function called!")
            _state.update { it.copy(isLoading = true, error = null) }

            try {
                loginUseCase(username, password).collect { result: Boolean ->
                    _state.update {
                        it.copy(isAuthenticated = result, isLoading = false)
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(error = e.message ?: "Login failed", isLoading = false)
                }
            }
        }
    }

    fun loginAsGuest() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            try {
                loginAsGuestUseCase().collect { result: Boolean ->
                    _state.update {
                        it.copy(isAuthenticated = result, isLoading = false)
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(error = e.message ?: "Guest login failed", isLoading = false)
                }
            }
        }
    }

    fun onCreateAccountClicked() {
        _state.update { it.copy(launchUrl = "https://www.themoviedb.org/signup") }
    }

    fun onForgotPasswordClicked() {
        _state.update { it.copy(launchUrl = "https://www.themoviedb.org/reset-password") }
    }
}

data class LoginState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val error: String? = null,
    val launchUrl: String? = null
)