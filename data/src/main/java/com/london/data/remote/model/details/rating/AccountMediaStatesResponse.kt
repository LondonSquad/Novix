package com.london.data.remote.model.details.rating

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AccountMediaStatesResponse(
    @SerialName("favorite")
    val favorite: Boolean? = null,
    @SerialName("id")
    val id: Int? = null,
    @SerialName("rated")
    val rated: RatingValue? = null,
    @SerialName("watchlist")
    val watchlist: Boolean? = null
)