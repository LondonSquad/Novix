package com.london.presentation.feature.details.tvshow.episodedetails

import com.london.domain.entity.Actor
import com.london.domain.entity.tvshowdetails.ImageItemEntity
import com.london.presentation.feature.base.ErrorState

data class EpisodeDetailsUiState(
    val tvImages: List<ImageItemEntity>? = listOf(),
    val isLoading: Boolean = true,
    val error: ErrorState? = null,
    val episodeGenres: List<String> = listOf(),
    val airDate: String = "",
    val episodeNumber: Int = 0,
    val seasonNumber: Int = 0,
    val episodeTypes: String = "",
    val tvShowId: Int = 0,
    val name: String = "",
    val overview: String = "",
    val stillPath: String = "",
    val voteAverage: Double = 0.0,
    val voteCount: Int = 0,
    val guestStars:List<Actor> = listOf(),
    val id: Int = 0,
    val backdropPath: String? = "",
    val haveTrailer: Boolean = false,
    val isSaved: Boolean = false
)
