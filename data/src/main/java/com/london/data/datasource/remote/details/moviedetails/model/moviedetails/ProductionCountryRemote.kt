package com.london.data.datasource.remote.details.moviedetails.model.moviedetails

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductionCountryRemote(
    @SerialName("iso_3166_1")
    val iso31661: String?,
    val name: String?
)
