package com.london.data.remote.model.details.rating

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RatingValue(
    @SerialName("value")
    val value: Int? = null
)