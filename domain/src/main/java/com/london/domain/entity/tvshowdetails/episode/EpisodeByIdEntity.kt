package com.london.domain.entity.tvshowdetails.episode

import com.london.domain.entity.Actor

data class EpisodeByIdEntity(
    val airDate: String?,
    val seasonNumber: Int,
    val tvShowId: Int,
    val name: String,
    val overview: String,
    val voteAverage: Double,
    val voteCount: Int,
    val guestStars: List<Actor>,
    val id: Int,
)
