package com.london.presentation.screen.onboarding

import android.util.Log
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.pager.PagerState
import androidx.lifecycle.viewModelScope
import com.london.domain.AppPreferencesService
import com.london.presentation.screen.base.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Provided
import kotlin.onFailure
import kotlin.runCatching

@KoinViewModel
class OnboardingViewModel(
    @Provided
    private val appPreferencesService: AppPreferencesService
) : BaseViewModel<OnboardingUiState, OnboardingEffect>(OnboardingUiState()) {


    fun onPageChanged(page: Int) {
        updateState { copy(currentPage = page) }
    }

    fun scrollToPage(pagerState: PagerState, targetPage: Int, scope: CoroutineScope) {
        scope.launch {
            if (targetPage in 0 until pagerState.pageCount) {
                pagerState.animateScrollToPage(
                    page = targetPage,
                    animationSpec = tween(durationMillis = 750, easing = FastOutSlowInEasing)
                )
            }
        }
    }

    fun scrollPrevious(pagerState: PagerState, scope: CoroutineScope) {
        val previousPage = pagerState.currentPage - 1
        if (previousPage >= 0) {
            scrollToPage(pagerState = pagerState, targetPage = previousPage, scope = scope)
        }
    }

    fun scrollNext(pagerState: PagerState, scope: CoroutineScope) {
        val nextPage = pagerState.currentPage + 1
        if (nextPage <= pagerState.pageCount - 1) {
            scrollToPage(pagerState = pagerState, targetPage = nextPage, scope = scope)
        } else {
            navigateToWelcome()
        }
    }

    fun navigateToWelcome() {
        emitEffect(OnboardingEffect.NavigateToWelcome)
    }

    fun onboardingFinished() {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { appPreferencesService.setOnBoardingShown() }
                .onFailure { Log.e("OnboardingViewModel", "onboardingFinished: ", it) }
        }
    }
}