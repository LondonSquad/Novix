package com.london.presentation.feature.search.details.tvshow.episode

sealed interface EpisodeDetailsEffect {
    data object NavigationBack : EpisodeDetailsEffect
    data class NavigateToCast(val episodeId: Int) : EpisodeDetailsEffect

    data object OnLoginNavigation : EpisodeDetailsEffect
}