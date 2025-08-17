package com.london.data.remote.model.list

import com.london.data.remote.model.search.MovieRemote
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ListDetailsResponse(
    @SerialName("id")
    val id: String? = null,
    @SerialName("item_count")
    val itemCount: Int? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("items")
    val items: List<MovieRemote>? = null,
)
