package com.london.data.datasource.remote.details.tvshowdetails.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TvShowDetailsRemoteResponse(
    @SerialName("adult")
    val adult: Boolean,
    @SerialName("backdrop_path")
    val backdropPath: String?,
    @SerialName("created_by")
    val createdBy: List<TvShowCreator>,
    @SerialName("episode_run_time")
    val episodeRunTime: List<Int>,
    @SerialName("first_air_date")
    val firstAirDate: String,
    @SerialName("tvShowGenres")
    val tvShowGenres: List<TvShowGenre>,
    @SerialName("homepage")
    val homepage: String,
    @SerialName("id")
    val id: Int,
    @SerialName("in_production")
    val inProduction: Boolean,
    @SerialName("language")
    val languages: List<String>,
    @SerialName("last_air_date")
    val lastAirDate: String,
    @SerialName("last_episode_to_air")
    val lastTvShowEpisodeToAir: TvShowEpisode?,
    @SerialName("name")
    val name: String,
    @SerialName("next_episode_to_air")
    val nextTvShowEpisodeToAir: TvShowEpisode?,
    @SerialName("tvShowNetworks")
    val tvShowNetworks: List<TvShowNetwork>,
    @SerialName("number_of_episodes")
    val numberOfEpisodes: Int,
    @SerialName("number_of_seasons")
    val numberOfSeasons: Int,
    @SerialName("origin_country")
    val originCountry: List<String>,
    @SerialName("original_language")
    val originalLanguage: String,
    @SerialName("original_name")
    val originalName: String,
    @SerialName("overview")
    val overview: String,
    @SerialName("popularity")
    val popularity: Double,
    @SerialName("poster_path")
    val posterPath: String?,
    @SerialName("production_companies")
    val productionCompanies: List<TvShowProductionCompany>,
    @SerialName("production_countries")
    val productionCountries: List<TvShowProductionCountry>,
    @SerialName("tvShowSeasons")
    val tvShowSeasons: List<TvShowSeason>,
    @SerialName("spoken_languages")
    val tvShowSpokenLanguages: List<TvShowSpokenLanguage>,
    @SerialName("status")
    val status: String,
    @SerialName("tagline")
    val tagline: String,
    @SerialName("type")
    val type: String,
    @SerialName("vote_average")
    val voteAverage: Double,
    @SerialName("vote_count")
    val voteCount: Int
)

@Serializable
data class TvShowCreator(
    val id: Int,
    @SerialName("credit_id")
    val creditId: String,
    @SerialName("name")
    val name: String,
    @SerialName("original_name")
    val originalName: String,
    @SerialName("gender")
    val gender: Int,
    @SerialName("profile_path")
    val profilePath: String?
)

@Serializable
data class TvShowGenre(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String
)

@Serializable
data class TvShowEpisode(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("overview")
    val overview: String,
    @SerialName("vote_average")
    val voteAverage: Double,
    @SerialName("vote_count")
    val voteCount: Int,
    @SerialName("air_date")
    val airDate: String,
    @SerialName("episode_number")
    val episodeNumber: Int,
    @SerialName("episode_type")
    val episodeType: String,
    @SerialName("production_code")
    val productionCode: String,
    @SerialName("runtime")
    val runtime: Int?,
    @SerialName("season_number")
    val seasonNumber: Int,
    @SerialName("show_id")
    val showId: Int,
    @SerialName("still_path")
    val stillPath: String?
)

@Serializable
data class TvShowNetwork(
    @SerialName("id")
    val id: Int,
    @SerialName("logo_path")
    val logoPath: String?,
    @SerialName("name")
    val name: String,
    @SerialName("origin_country")
    val originCountry: String
)

@Serializable
data class TvShowProductionCompany(
    val id: Int,
    @SerialName("logo_path")
    val logoPath: String?,
    @SerialName("name")
    val name: String,
    @SerialName("origin_country")
    val originCountry: String
)

@Serializable
data class TvShowProductionCountry(
    @SerialName("iso_3166_1")
    val iso31661: String,
    @SerialName("name")
    val name: String
)

@Serializable
data class TvShowSeason(
    @SerialName("air_date")
    val airDate: String?,
    @SerialName("episode_count")
    val episodeCount: Int,
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("overview")
    val overview: String,
    @SerialName("poster_path")
    val posterPath: String?,
    @SerialName("season_number")
    val seasonNumber: Int,
    @SerialName("vote_average")
    val voteAverage: Double
)

@Serializable
data class TvShowSpokenLanguage(
    @SerialName("english_name")
    val englishName: String,
    @SerialName("iso_639_1")
    val iso6391: String,
    @SerialName("name")
    val name: String
)