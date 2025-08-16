package com.london.presentation.feature.details.tvshow.info

import com.london.domain.entity.tvshowdetails.TvShowCastEntity
import com.london.domain.entity.tvshowdetails.episode.TvShowEpisodeBySeasonEntity
import com.london.domain.entity.tvshowdetails.episode.TvShowEpisodesEntity
import com.london.presentation.shared.base.ErrorState
import com.london.presentation.shared.genre.TvShowGenreUi

data class TvShowDetailsUiState(
    val id: Int = 0,
    val name: String = "",
    val overview: String = "",
    val selectedRating: Int = 0,
    val isSaved: Boolean = false,
    val isError: Boolean = false,
    val isRated: Boolean = false,
    val numberOfSeasons: Int = 0,
    val voteAverage: Double = 0.0,
    val firstAirDate: String = "",
    val error: ErrorState? = null,
    val videoProvider: String = "",
    val isLoading: Boolean = false,
    val isGuestUser: Boolean = false,
    val cast: TvShowCastEntity? = null,
    val isSuccessfullyRated: Boolean? = null,
    val isRateBottomSheetVisible: Boolean = false,
    val tvImages: List<String>? = listOf(),
    val isGuestUserBottomSheetVisible: Boolean = false,
    val tvShowGenres: List<TvShowGenreUi> = listOf(),
    val tvShowEpisodeCountBySeason: TvShowEpisodesEntity? = null,
    val tvShowEpisodes: List<TvShowEpisodeBySeasonEntity> = listOf(),
) {
    val movieHaveTrailer: Boolean
        get() = videoProvider.isNotEmpty()
}
