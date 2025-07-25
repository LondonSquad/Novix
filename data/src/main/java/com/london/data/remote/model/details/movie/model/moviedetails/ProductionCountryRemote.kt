package com.london.data.remote.model.details.movie.model.moviedetails

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductionCountryRemote(
    @SerialName("iso_3166_1")
    val iso31661: String?,
    @SerialName("name")
    val name: String?
)
