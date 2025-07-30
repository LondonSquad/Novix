package com.london.presentation.feature.continuewatching

sealed interface ContinueWatchingEffect {
    data object NavigateBack : ContinueWatchingEffect
    data class NavigateToMovieDetails(val id: Int) : ContinueWatchingEffect
    data class NavigateToTvShowDetails(val id: Int) : ContinueWatchingEffect
}
