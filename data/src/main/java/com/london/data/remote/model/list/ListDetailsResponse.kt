package com.london.data.remote.model.list

import com.london.data.remote.model.search.MovieRemote
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ListDetailsResponse(
    @SerialName("created_by")
    val createdBy: String?,
    @SerialName("description")
    val description: String?,
    @SerialName("favorite_count")
    val favoriteCount: Int?,
    @SerialName("id")
    val id: String?,
    @SerialName("iso_639_1")
    val iso6391: String?,
    @SerialName("item_count")
    val itemCount: Int?,
    @SerialName("name")
    val name: String?,
    @SerialName("poster_path")
    val posterPath: String?,
    @SerialName("items")
    val items: List<MovieRemote>?,
)
