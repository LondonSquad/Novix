package com.london.presentation.feature.details.tvshow.episode

sealed interface EpisodeDetailsEffect {
    data object BackNavigation : EpisodeDetailsEffect
    data class LoginNavigation(
        val tvShowId: Int,
        val seasonNumber: Int,
        val episodeNumber: Int
    ) :
        EpisodeDetailsEffect

    data class CastNavigation(val episodeId: Int) : EpisodeDetailsEffect
}
