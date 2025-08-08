package com.london.presentation.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.london.domain.AppPreferencesService
import com.london.domain.usecase.authentication.AuthenticationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authenticationUseCase: AuthenticationUseCase,
    private val appPreferencesService: AppPreferencesService
) : ViewModel() {

    private val _effect = MutableSharedFlow<SplashEffect?>()
    val effect = _effect.asSharedFlow()

    init {
        determineStartDestination()
    }

    private fun determineStartDestination() {
        viewModelScope.launch {
            delay(1500)
            val destination = when {
                !appPreferencesService.hasOnboardingBeenShown -> SplashEffect.Onboarding
                authenticationUseCase.isLoggedIn() -> SplashEffect.Home
                else -> SplashEffect.Welcome
            }

            _effect.emit(destination)
        }
    }
}