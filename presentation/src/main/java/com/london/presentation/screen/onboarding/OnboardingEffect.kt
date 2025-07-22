package com.london.presentation.screen.onboarding

sealed interface OnboardingEffect {
    data object NavigateToWelcome : OnboardingEffect
    data object SkipOnboarding : OnboardingEffect
    data class ScrollToPage(val page: Int) : OnboardingEffect
}
