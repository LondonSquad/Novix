package com.london.presentation.feature.onboarding

import androidx.compose.runtime.Composable

@Composable
fun OnboardingRoute(
    onNavigateToWelcome: () -> Unit,
) {
    OnboardingScreen(
        onComplete = {
            onNavigateToWelcome()
        }
    )
}
