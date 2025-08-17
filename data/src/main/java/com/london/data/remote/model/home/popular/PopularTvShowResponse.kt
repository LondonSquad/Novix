package com.london.data.remote.model.home.popular

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PopularTvShowResponse(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("poster_path")
    val posterPath: String? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("vote_average")
    val voteAverage: Double? = null,
)
