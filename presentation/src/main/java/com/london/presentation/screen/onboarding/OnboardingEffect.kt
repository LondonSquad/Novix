package com.london.presentation.screen.onboarding

sealed interface OnboardingEffect {
    data object NavigateToWelcome : OnboardingEffect
    data class ScrollToPage(val page: Int) : OnboardingEffect
}
