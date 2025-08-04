package com.london.data.remote.model.details.movie.model.moviedetails


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class AccountMovieStatesResponse(
    @SerialName("favorite")
    val favorite: Boolean? = null,
    @SerialName("id")
    val id: Int? = null,
    @SerialName("rated")
    val rated: JsonElement? = null,
    @SerialName("watchlist")
    val watchlist: Boolean? = null
)