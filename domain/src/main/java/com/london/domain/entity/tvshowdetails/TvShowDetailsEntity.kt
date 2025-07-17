package com.london.domain.entity.tvshowdetails

data class TvShowDetailsEntity(
    val adult: Boolean,
    val backdropUrl: String?,
    val createdBy: List<TvShowCreatorEntity>,
    val episodeRunTime: List<Int>,
    val firstAirDate: String,
    val tvShowGenres: List<TvShowGenreEntity>,
    val homepage: String,
    val id: Int,
    val inProduction: Boolean,
    val languages: List<String>,
    val lastAirDate: String,
    val lastTvShowEpisodeToAir: TvShowEpisodeEntity?,
    val name: String,
    val nextTvShowEpisodeToAir: TvShowEpisodeEntity?,
    val tvShowNetworks: List<TvShowNetworkEntity>,
    val numberOfEpisodes: Int,
    val numberOfSeasons: Int,
    val originCountry: List<String>,
    val originalLanguage: String,
    val originalName: String,
    val overview: String,
    val popularity: Double,
    val posterUrl: String?,
    val productionCompanies: List<TvShowProductionCompanyEntity>,
    val productionCountries: List<TvShowProductionCountryEntity>,
    val tvShowSeasons: List<TvShowSeasonEntity>,
    val tvShowSpokenLanguageEntities: List<TvShowSpokenLanguageEntity>,
    val status: String,
    val tagline: String,
    val type: String,
    val voteAverage: Double,
    val voteCount: Int
)

data class TvShowCreatorEntity(
    val id: Int,
    val creditId: String,
    val name: String,
    val originalName: String,
    val gender: Int,
    val profileUrl: String?
)

data class TvShowGenreEntity(
    val id: Int,
    val name: String
)

data class TvShowEpisodeEntity(
    val id: Int,
    val name: String,
    val overview: String,
    val voteAverage: Double,
    val voteCount: Int,
    val airDate: String,
    val episodeNumber: Int,
    val episodeType: String,
    val productionCode: String,
    val runtime: Int?,
    val seasonNumber: Int,
    val showId: Int,
    val stillPath: String?
)

data class TvShowNetworkEntity(
    val id: Int,
    val logoUrl: String?,
    val name: String,
    val originCountry: String
)

data class TvShowProductionCompanyEntity(
    val id: Int,
    val logoUrl: String?,
    val name: String,
    val originCountry: String
)

data class TvShowProductionCountryEntity(
    val iso31661: String,
    val name: String
)

data class TvShowSeasonEntity(
    val airDate: String?,
    val episodeCount: Int,
    val id: Int,
    val name: String,
    val overview: String,
    val posterUrl: String?,
    val seasonNumber: Int,
    val voteAverage: Double
)

data class TvShowSpokenLanguageEntity(
    val englishName: String,
    val iso6391: String,
    val name: String
)
