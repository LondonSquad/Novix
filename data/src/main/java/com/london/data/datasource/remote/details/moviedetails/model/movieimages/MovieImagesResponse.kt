package com.london.data.datasource.remote.details.moviedetails.model.movieimages

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class MovieImagesResponse(
    @SerialName("backdrops")
    val backdrops: List<Backdrop>?,
    @SerialName("id")
    val id: Int?,
    @SerialName("logos")
    val logos: List<Logo>?,
    @SerialName("posters")
    val posters: List<Poster>?
)