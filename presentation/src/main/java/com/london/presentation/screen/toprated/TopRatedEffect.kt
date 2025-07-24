package com.london.presentation.screen.toprated

sealed interface TopRatedEffect {
    data object NavigateBack : TopRatedEffect
    data object NavigateToMovieDetails : TopRatedEffect
}