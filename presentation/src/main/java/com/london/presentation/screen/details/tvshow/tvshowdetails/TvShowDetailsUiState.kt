package com.london.presentation.screen.details.tvshow.tvshowdetails

import com.london.domain.entity.CastEntity
import com.london.domain.entity.ImageItemEntity
import com.london.domain.entity.TvShowCreatorEntity
import com.london.domain.entity.TvShowEpisodeEntity
import com.london.domain.entity.TvShowGenreEntity
import com.london.domain.entity.TvShowNetworkEntity
import com.london.domain.entity.TvShowProductionCompanyEntity
import com.london.domain.entity.TvShowProductionCountryEntity
import com.london.domain.entity.TvShowSeasonEntity
import com.london.domain.entity.TvShowSpokenLanguageEntity

data class TvShowDetailsUiState(
    val tvImages: List<String>? = listOf(),
    val adult: Boolean = false,
    val cast: CastEntity? = null,
    val backdropPath: String? = "",
    val createdBy: List<TvShowCreatorEntity> = listOf(),
    val episodeRunTime: List<Int> = listOf(),
    val firstAirDate: String = "",
    val tvShowGenres: List<TvShowGenreEntity> = listOf(),
    val homepage: String = "",
    val id: Int = 0,
    val inProduction: Boolean = false,
    val languages: List<String> = listOf(),
    val lastAirDate: String = "",
    val lastTvShowEpisodeToAir: TvShowEpisodeEntity? = null,
    val name: String = "",
    val nextTvShowEpisodeToAir: TvShowEpisodeEntity? = null,
    val tvShowNetworks: List<TvShowNetworkEntity> = listOf(),
    val numberOfEpisodes: Int = 0,
    val numberOfSeasons: Int = 0,
    val originCountry: List<String> = listOf(),
    val originalLanguage: String = "",
    val originalName: String = "",
    val overview: String = "",
    val popularity: Double = 0.0,
    val posterPath: String? = "",
    val productionCompanies: List<TvShowProductionCompanyEntity> = listOf(),
    val productionCountries: List<TvShowProductionCountryEntity> = listOf(),
    val tvShowSeasons: List<TvShowSeasonEntity> = listOf(),
    val tvShowSpokenLanguages: List<TvShowSpokenLanguageEntity> = listOf(),
    val status: String = "",
    val tagline: String = "",
    val type: String = "",
    val voteAverage: Double = 0.0,
    val voteCount: Int = 0
)
