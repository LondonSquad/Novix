package com.london.domain.entity.tvshowdetails.episode

import com.london.domain.entity.Actor

data class TvShowEpisodeByIdEntity(
    val airDate: String?,
    val episodeNumber: Int,
    val seasonNumber: Int,
    val episodeTypes: String,
    val tvShowId: Int,
    val name: String,
    val overview: String,
    val stillPath: String?,
    val voteAverage: Double,
    val voteCount: Int,
    val guestStars: List<Actor>,
    val id: Int,
    )
