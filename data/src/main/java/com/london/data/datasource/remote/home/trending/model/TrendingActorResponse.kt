// DTOs for trending actors API response
package com.london.data.datasource.remote.home.trending.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrendingActorResponse(
    @SerialName("results")
    val results: List<TrendingActorDto>,
    @SerialName("page")
    val page: Int,
    @SerialName("total_pages")
    val totalPages: Int
)

@Serializable
data class TrendingActorDto(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String?,
    @SerialName("profile_path")
    val profilePath: String?,
    @SerialName("popularity")
    val popularity: Double?
) 