package com.london.data.datasource.remote.details.moviedetails.model.moviedetails

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenreRemote(
    @SerialName("id")
    val id: Int?,
    @SerialName("name")
    val name: String?
)