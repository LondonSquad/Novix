package com.london.presentation.feature.splash

import androidx.compose.runtime.Composable

@Composable
fun SplashRoute(
    onNavigateToOnboarding: () -> Unit,
    onNavigateToWelcome: () -> Unit,
    onNavigateToHome: () -> Unit,
) {
    SplashScreen(
        onNavigateToOnboarding = onNavigateToOnboarding,
        onNavigateToHome = onNavigateToHome,
        onNavigateToWelcome = onNavigateToWelcome
    )
}
