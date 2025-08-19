package com.london.data.remote.model.popular

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PopularMovieResponse(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("poster_path")
    val posterPath: String? = null,
    @SerialName("title")
    val title: String? = null,
    @SerialName("vote_average")
    val voteAverage: Double? = null,
)
