package com.london.data.remote.model.details.movie.model.moviedetails

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetailsResponse(
    @SerialName("backdrop_path")
    val backdropPath: String? = null,
    @SerialName("genres")
    val genreRemote: List<GenreRemote>? = null,
    @SerialName("id")
    val id: Int? = null,
    @SerialName("overview")
    val overview: String? = null,
    @SerialName("poster_path")
    val posterPath: String? = null,
    @SerialName("release_date")
    val releaseDate: String? = null,
    @SerialName("runtime")
    val runtime: Int? = null,
    @SerialName("title")
    val title: String? = null,
    @SerialName("video")
    val video: Boolean? = null,
    @SerialName("vote_average")
    val voteAverage: Double? = null,
)
