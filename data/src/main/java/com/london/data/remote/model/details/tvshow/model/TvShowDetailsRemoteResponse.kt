package com.london.data.remote.model.details.tvshow.model

import com.london.data.mapper.genre.GenreMapper
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TvShowDetailsRemoteResponse(
    @SerialName("first_air_date")
    val firstAirDate: String? = null,
    @SerialName("genres")
    val tvShowGenres: List<TvShowGenre>? = null,
    @SerialName("id")
    val id: Int? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("number_of_episodes")
    val numberOfEpisodes: Int? = null,
    @SerialName("number_of_seasons")
    val numberOfSeasons: Int? = null,
    @SerialName("overview")
    val overview: String? = null,
    @SerialName("poster_path")
    val posterPath: String? = null,
    @SerialName("seasons")
    val tvShowSeasons: List<TvShowSeason>? = null,
    @SerialName("vote_average")
    val voteAverage: Double? = null,
    @SerialName("vote_count")
    val voteCount: Int? = null
) : GenreMapper

@Serializable
data class TvShowGenre(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("name")
    val name: String? = null
)

@Serializable
data class TvShowSeason(
    @SerialName("season_number")
    val seasonNumber: Int? = null,
)