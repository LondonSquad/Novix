package com.london.presentation.feature.onboarding

import androidx.compose.runtime.Composable
import com.london.domain.AppPreferencesService

@Composable
fun OnboardingRoute(
    onNavigateToWelcome: () -> Unit,
     appPreferencesService: AppPreferencesService
) {
    OnboardingScreen(
        onComplete = {
            appPreferencesService.setOnBoardingShown()
            onNavigateToWelcome()
        }
    )
}
