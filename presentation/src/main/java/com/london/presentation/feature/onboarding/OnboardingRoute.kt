package com.london.presentation.feature.onboarding

import androidx.compose.runtime.Composable
import com.london.domain.AppPreferencesService
import org.koin.core.annotation.Provided

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
