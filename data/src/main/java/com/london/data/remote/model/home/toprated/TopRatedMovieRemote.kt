package com.london.data.remote.model.home.toprated


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TopRatedMovieRemote(
    @SerialName("genre_ids")
    val genreIds: List<Int>? = null,
    @SerialName("id")
    val id: Int? = null,
    @SerialName("poster_path")
    val posterPath: String? = null,
    @SerialName("release_date")
    val releaseDate: String? = null ,
    @SerialName("title")
    val title: String? = null,
    @SerialName("vote_average")
    val voteAverage: Double? = null,
)
