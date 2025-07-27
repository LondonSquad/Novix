package com.london.presentation.screen.onboarding

sealed class SplashEffect {
    object Onboarding : SplashEffect()
    object Welcome : SplashEffect()
    object Home : SplashEffect()
}