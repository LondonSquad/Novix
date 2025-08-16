package com.london.data.remote.model.details.movie.model.movieimages

import com.london.data.remote.model.details.ImageItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class MovieImagesResponse(
    @SerialName("backdrops")
    val backdrops: List<ImageItem>?,
    @SerialName("id")
    val id: Int?,
    @SerialName("logos")
    val logos: List<ImageItem>?,
    @SerialName("posters")
    val posters: List<ImageItem>?
)