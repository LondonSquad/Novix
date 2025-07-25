package com.london.data.remote.model.details.movie.model.moviedetails

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenreRemote(
    @SerialName("id")
    val id: Int?,
    @SerialName("name")
    val name: String?
)