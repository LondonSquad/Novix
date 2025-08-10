package com.london.presentation.feature.details.tvshow.episode

sealed interface EpisodeDetailsEffect {
    data object NavigationBack : EpisodeDetailsEffect
    data object OnLoginNavigation : EpisodeDetailsEffect
    data class NavigateToCast(val episodeId: Int) : EpisodeDetailsEffect
}