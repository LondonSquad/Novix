package com.london.presentation.screen.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.london.domain.AppPreferencesService
import com.london.domain.repository.AuthRepository
import kotlinx.coroutines.delay
import org.koin.core.annotation.Provided

@Composable
fun SplashRoute(
    onNavigateToOnboarding: () -> Unit,
    onNavigateToWelcome: () -> Unit,
    onNavigateToHome: () -> Unit,
    @Provided appPreferencesService: AppPreferencesService,
    @Provided authRepository: AuthRepository
) {
    SplashScreen()

    LaunchedEffect(Unit) {
        delay(1500)

        val destination = when {
            !appPreferencesService.hasOnboardingBeenShown -> SplashEffect.Onboarding
            authRepository.isLoggedIn() -> SplashEffect.Home
            else -> SplashEffect.Welcome
        }
        when (destination) {
            SplashEffect.Onboarding -> onNavigateToOnboarding()
            SplashEffect.Home -> onNavigateToHome()
            SplashEffect.Welcome -> onNavigateToWelcome()
        }
    }
}
