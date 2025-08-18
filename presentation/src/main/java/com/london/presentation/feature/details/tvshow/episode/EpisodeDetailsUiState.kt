package com.london.presentation.feature.details.tvshow.episode

import com.london.domain.entity.tvshowdetails.episode.EpisodeDetails
import com.london.presentation.shared.base.ErrorState

data class EpisodeDetailsUiState(
    val selectedRating: Int = 0,
    val isRated: Boolean = false,
    val isLoading: Boolean = true,
    val error: ErrorState? = null,
    val videoProvider: String = "",
    val isGuestUser: Boolean = false,
    val images: List<String>? = listOf(),
    val episode: EpisodeDetails? = null,
    val isSuccessfullyRated: Boolean? = null,
    val episodeGenres: List<String> = listOf(),
    val isRateBottomSheetVisible: Boolean = false,
    val isGuestUserBottomSheetVisible: Boolean = false,
){
    val episodeHaveTrailer: Boolean
        get() = videoProvider.isNotEmpty()
}
