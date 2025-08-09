package com.london.presentation.feature.home.continuewatching

sealed interface ContinueWatchingEffect {
    data object NavigateBack : ContinueWatchingEffect
    data class NavigateToMovieDetails(val id: Int) : ContinueWatchingEffect
    data class NavigateToTvShowDetails(val id: Int) : ContinueWatchingEffect
}
