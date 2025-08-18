package com.london.presentation.feature.details.tvshow.episode

sealed interface EpisodeDetailsEffect {
    data object BackNavigation : EpisodeDetailsEffect
    data object LoginNavigation : EpisodeDetailsEffect
    data class CastNavigation(val episodeId: Int) : EpisodeDetailsEffect
}