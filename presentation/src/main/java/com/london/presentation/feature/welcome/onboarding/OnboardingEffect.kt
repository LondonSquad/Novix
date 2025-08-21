package com.london.presentation.feature.welcome.onboarding

sealed interface OnboardingEffect {
    data object WelcomeNavigation : OnboardingEffect
    data class ScrollToPage(val page: Int) : OnboardingEffect
}
