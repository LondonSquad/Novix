package com.london.domain.entity.tvshow.episode

import com.london.domain.entity.actor.Actor

data class EpisodeDetails(
    val airDate: String,
    val seasonNumber: Int,
    val tvShowId: Int,
    val name: String,
    val overview: String,
    val voteAverage: Double,
    val voteCount: Int,
    val guestStars: List<Actor>,
    val id: Int,
)
