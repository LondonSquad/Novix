package com.london.presentation.screen.onboarding

import android.util.Log
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.pager.PagerState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.london.domain.AppPreferencesService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import kotlin.onFailure
import kotlin.runCatching

@KoinViewModel
class OnboardingViewModel(
    private val appPreferencesService: AppPreferencesService
) : ViewModel() {

    private val _currentPage = MutableStateFlow(0)
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()

    fun onPageChanged(page: Int) {
        _currentPage.value = page
    }

    fun scrollPrevious(pagerState: PagerState) {
        viewModelScope.launch {
            val previousPage = pagerState.currentPage - 1
            if (previousPage >= 0) {
                pagerState.animateScrollToPage(previousPage)
            }
        }
    }

    fun scrollNext(pagerState: PagerState) {
        viewModelScope.launch {
            val nextPage = pagerState.currentPage + 1
            if (nextPage <= pagerState.pageCount - 1) {
                pagerState.scrollToPage(pagerState.currentPage)
                pagerState.animateScrollToPage(
                    page = nextPage,
                    animationSpec = tween(durationMillis = 750, easing = FastOutSlowInEasing)
                )
            }
        }
    }

    fun onboardingFinished() {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { appPreferencesService.setOnBoardingShown() }
                .onFailure { Log.e("OnboardingViewModel", "onboardingFinished: ", it) }
        }
    }
}