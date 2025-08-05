package com.london.data.remote.model.list

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ListMovieBody(

    @SerialName("media_id")
    val mediaId: Int,
)
