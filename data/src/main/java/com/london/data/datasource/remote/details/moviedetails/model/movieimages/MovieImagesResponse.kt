package com.london.data.datasource.remote.details.moviedetails.model.movieimages

import kotlinx.serialization.Serializable


@Serializable
data class MovieImagesResponse(
    val backdrops: List<Backdrop>?,
    val id: Int?,
    val logos: List<Logo>?,
    val posters: List<Poster>?
)