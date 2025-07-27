package com.london.presentation.feature.toprated

sealed interface TopRatedEffect {
    data object NavigateBack : TopRatedEffect
    data class NavigateToMovieDetails(val id: Int) : TopRatedEffect
    data class NavigateToTvShowDetails(val id: Int) : TopRatedEffect
}