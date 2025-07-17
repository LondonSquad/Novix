package com.london.data.datasource.remote.details.moviedetails.model.moviecast

import com.london.data.datasource.remote.details.moviedetails.model.movieimages.Logo
import com.london.data.datasource.remote.details.moviedetails.model.movieimages.Poster
import kotlinx.serialization.Serializable

@Serializable
data class MovieImages(
    val backdrops: List<Backdrop>?,
    val id: Int?,
    val logos: List<Logo>?,
    val posters: List<Poster>?
)