package com.london.presentation.feature.onboarding

sealed class SplashEffect {
    object Onboarding : SplashEffect()
    object Welcome : SplashEffect()
    object Home : SplashEffect()
}