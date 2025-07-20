package com.london.presentation.screen.onboarding

import com.london.presentation.R
import androidx.annotation.StringRes

data class OnboardingPage(
    @StringRes val title: Int,
    @StringRes val description: Int,
    val imageRes: Int
)

val onboardingPages = listOf(
    OnboardingPage(R.string.first_onboarding_title, R.string.first_onboarding_description, R.drawable.image_onboarding_first),
    OnboardingPage(R.string.second_onboarding_title, R.string.second_onboarding_description, R.drawable.image_onboarding_second),
    OnboardingPage(R.string.third_onboarding_title, R.string.third_onboarding_description, R.drawable.image_onboarding_third),
)
