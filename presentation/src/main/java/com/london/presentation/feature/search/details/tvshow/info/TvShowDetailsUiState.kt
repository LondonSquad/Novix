package com.london.presentation.feature.search.details.tvshow.info

import com.london.domain.entity.tvshowdetails.ImageItemEntity
import com.london.domain.entity.tvshowdetails.TvShowCastEntity
import com.london.domain.entity.tvshowdetails.TvShowCreatorEntity
import com.london.domain.entity.tvshowdetails.TvShowEpisodeEntity
import com.london.domain.entity.tvshowdetails.TvShowGenreEntity
import com.london.domain.entity.tvshowdetails.TvShowNetworkEntity
import com.london.domain.entity.tvshowdetails.TvShowProductionCompanyEntity
import com.london.domain.entity.tvshowdetails.TvShowProductionCountryEntity
import com.london.domain.entity.tvshowdetails.TvShowSeasonEntity
import com.london.domain.entity.tvshowdetails.TvShowSpokenLanguageEntity
import com.london.domain.entity.tvshowdetails.episode.TvShowEpisodeBySeasonEntity
import com.london.domain.entity.tvshowdetails.episode.TvShowEpisodesEntity
import com.london.presentation.shared.base.ErrorState

data class TvShowDetailsUiState(
    val id: Int = 0,
    val name: String = "",
    val type: String = "",
    val voteCount: Int = 0,
    val status: String = "",
    val tagline: String = "",
    val homepage: String = "",
    val overview: String = "",
    val adult: Boolean = false,
    val selectedRating: Int = 0,
    val isSaved: Boolean = false,
    val isError: Boolean = false,
    val isRated: Boolean = false,
    val lastAirDate: String = "",
    val posterPath: String? = "",
    val numberOfSeasons: Int = 0,
    val popularity: Double = 0.0,
    val voteAverage: Double = 0.0,
    val numberOfEpisodes: Int = 0,
    val originalName: String = "",
    val firstAirDate: String = "",
    val error: ErrorState? = null,
    val backdropPath: String? = "",
    val videoProvider: String = "",
    val isLoading: Boolean = false,
    val isGuestUser: Boolean = false,
    val inProduction: Boolean = false,
    val originalLanguage: String = "",
    val cast: TvShowCastEntity? = null,
    val languages: List<String> = listOf(),
    val isSuccessfullyRated: Boolean? = null,
    val episodeRunTime: List<Int> = listOf(),
    val originCountry: List<String> = listOf(),
    val isRateBottomSheetVisible: Boolean = false,
    val tvImages: List<ImageItemEntity>? = listOf(),
    val isGuestUserBottomSheetVisible: Boolean = false,
    val createdBy: List<TvShowCreatorEntity> = listOf(),
    val tvShowGenres: List<TvShowGenreEntity> = listOf(),
    val tvShowSeasons: List<TvShowSeasonEntity> = listOf(),
    val nextTvShowEpisodeToAir: TvShowEpisodeEntity? = null,
    val lastTvShowEpisodeToAir: TvShowEpisodeEntity? = null,
    val tvShowNetworks: List<TvShowNetworkEntity> = listOf(),
    val tvShowEpisodeCountBySeason: TvShowEpisodesEntity? = null,
    val tvShowEpisodes: List<TvShowEpisodeBySeasonEntity> = listOf(),
    val productionCompanies: List<TvShowProductionCompanyEntity> = listOf(),
    val productionCountries: List<TvShowProductionCountryEntity> = listOf(),
    val tvShowSpokenLanguages: List<TvShowSpokenLanguageEntity> = listOf(),
) {
    val movieHaveTrailer: Boolean
        get() = videoProvider.isNotEmpty()
}
