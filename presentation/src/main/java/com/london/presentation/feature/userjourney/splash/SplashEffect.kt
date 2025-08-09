package com.london.presentation.feature.userjourney.splash

sealed class SplashEffect {
    object Onboarding : SplashEffect()
    object Welcome : SplashEffect()
    object Home : SplashEffect()
}