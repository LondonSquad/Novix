package com.london.presentation.feature.watching

sealed class WatchingMediaEffect {
    data class NavigateToMovieDetails(val id: Int) : WatchingMediaEffect()
    data class NavigateToTvShowDetails(val id: Int) : WatchingMediaEffect()
    data object NavigateBack : WatchingMediaEffect()
}