package com.london.presentation.feature.userjourney.splash

sealed class SplashEffect {
    data object Home : SplashEffect()
    data object Welcome : SplashEffect()
    data object Onboarding : SplashEffect()
}