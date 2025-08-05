package com.london.presentation.feature.splash

sealed class SplashEffect {
    object Onboarding : SplashEffect()
    object Welcome : SplashEffect()
    object Home : SplashEffect()
}