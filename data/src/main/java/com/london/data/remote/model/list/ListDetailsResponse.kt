package com.london.data.remote.model.list

import com.london.data.remote.model.search.MovieRemote
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ListDetailsResponse(
    @SerialName("created_by")
    val createdBy: String? = null,
    @SerialName("description")
    val description: String? = null,
    @SerialName("favorite_count")
    val favoriteCount: Int? = null,
    @SerialName("id")
    val id: String? = null,
    @SerialName("iso_639_1")
    val iso6391: String? = null,
    @SerialName("item_count")
    val itemCount: Int? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("poster_path")
    val posterPath: String? = null,
    @SerialName("items")
    val items: List<MovieRemote>? = null,
)
