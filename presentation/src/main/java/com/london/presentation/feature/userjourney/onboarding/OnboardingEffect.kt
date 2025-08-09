package com.london.presentation.feature.userjourney.onboarding

sealed interface OnboardingEffect {
    data object NavigateToWelcome : OnboardingEffect
    data class ScrollToPage(val page: Int) : OnboardingEffect
}
