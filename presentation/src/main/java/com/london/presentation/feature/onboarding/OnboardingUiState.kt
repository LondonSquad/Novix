package com.london.presentation.feature.onboarding

import androidx.annotation.StringRes
import com.london.presentation.R

data class OnboardingUiState(
    val currentPage: Int = 0,
    val pages: List<OnboardingPage> = OnboardingPage.defaultPages
) {
    val isFirstPage = currentPage == 0
    val isLastPage = currentPage == pages.lastIndex
}


data class OnboardingPage(
    @StringRes val title: Int,
    @StringRes val description: Int,
    val imageRes: Int
) {
    companion object {
        val defaultPages = listOf(
            OnboardingPage(
                title = R.string.first_onboarding_title,
                description = R.string.first_onboarding_description,
                imageRes = R.drawable.img_onboarding_first
            ),
            OnboardingPage(
                title = R.string.second_onboarding_title,
                description = R.string.second_onboarding_description,
                imageRes = R.drawable.img_onboarding_second
            ),
            OnboardingPage(
                title = R.string.third_onboarding_title,
                description = R.string.third_onboarding_description,
                imageRes = R.drawable.img_onboarding_third
            )
        )
    }
}
