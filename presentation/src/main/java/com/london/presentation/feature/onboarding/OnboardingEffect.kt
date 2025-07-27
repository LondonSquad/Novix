package com.london.presentation.feature.onboarding

sealed interface OnboardingEffect {
    data object NavigateToWelcome : OnboardingEffect
    data class ScrollToPage(val page: Int) : OnboardingEffect
}
