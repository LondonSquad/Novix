package com.london.data.remote.model.details.rating

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RatingRemoteBody(
    @SerialName("value")
    val value: Int? = null
)
