package com.london.presentation.screen.details.tvshow.episodedetails

sealed interface EpisodeDetailsEffect {
    data object NavigationBack : EpisodeDetailsEffect
}