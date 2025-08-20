package com.london.presentation.feature.welcome.splash

import androidx.compose.runtime.Composable

@Composable
fun SplashRoute(
    onNavigateToHome: () -> Unit,
    onNavigateToWelcome: () -> Unit,
    onNavigateToOnboarding: () -> Unit,
) {
    SplashScreen(
        onNavigateToOnboarding = onNavigateToOnboarding,
        onNavigateToHome = onNavigateToHome,
        onNavigateToWelcome = onNavigateToWelcome
    )
}
