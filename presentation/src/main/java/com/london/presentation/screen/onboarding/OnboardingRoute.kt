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
        val hasSeenOnboarding = appPreferencesService.hasOnboardingBeenShown
        if (!hasSeenOnboarding) {
            onNavigateToOnboarding()
        } else {
            val isLoggedIn = authRepository.isLoggedIn()
            if (isLoggedIn) {
                onNavigateToHome()
            } else {
                onNavigateToWelcome()
            }
        }
    }
}

@Composable
fun OnboardingRoute(
    onNavigateToWelcome: () -> Unit,
    @Provided appPreferencesService: AppPreferencesService
) {
    OnboardingScreen(
        onComplete = {
            appPreferencesService.setOnBoardingShown()
            onNavigateToWelcome()
        }
    )
}
