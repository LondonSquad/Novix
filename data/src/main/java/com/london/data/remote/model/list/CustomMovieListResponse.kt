package com.london.data.remote.model.list

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CustomMovieListResponse(
    @SerialName("description")
    val description: String? = null,
    @SerialName("id")
    val id: Int? = null,
    @SerialName("item_count")
    val itemCount: Int? = null,
    @SerialName("name")
    val name: String? = null,
)
