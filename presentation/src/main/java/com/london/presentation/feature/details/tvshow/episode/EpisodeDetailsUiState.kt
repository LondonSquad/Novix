package com.london.presentation.feature.details.tvshow.episode

import com.london.domain.entity.Actor
import com.london.presentation.shared.base.ErrorState

data class EpisodeDetailsUiState(
    val tvImages: List<String>? = listOf(),
    val isLoading: Boolean = true,
    val error: ErrorState? = null,
    val episodeGenres: List<String> = listOf(),
    val airDate: String = "",
    val seasonNumber: Int = 0,
    val tvShowId: Int = 0,
    val name: String = "",
    val overview: String = "",
    val voteAverage: Double = 0.0,
    val voteCount: Int = 0,
    val guestStars:List<Actor> = listOf(),
    val id: Int = 0,
    val backdropPath: String? = "",
    val isSaved: Boolean = false,
    val videoProvider: String = "",
    val isRateBottomSheetVisible: Boolean = false,
    val selectedRating: Int = 0,
    val isSuccessfullyRated: Boolean? = null,
    val isGuestUserBottomSheetVisible: Boolean = false,
    val isGuestUser: Boolean = false,
    val isRated: Boolean = false
){
    val episodeHaveTrailer: Boolean
        get() = videoProvider.isNotEmpty()
}
