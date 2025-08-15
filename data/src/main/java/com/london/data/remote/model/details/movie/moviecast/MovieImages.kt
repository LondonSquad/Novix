package com.london.data.remote.model.details.movie.moviecast


import com.london.data.remote.model.details.movie.movieimages.Logo
import com.london.data.remote.model.details.movie.movieimages.Poster
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieImages(
    @SerialName("backdrops")
    val backdrops: List<Backdrop>?,
    @SerialName("id")
    val id: Int?,
    @SerialName("logos")
    val logos: List<Logo>?,
    @SerialName("posters")
    val posters: List<Poster>?
)