package com.london.data.datasource.remote.home.trending.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrendingTvShowListResponse(
    @SerialName("results")
    val results: List<TrendingTvShowResponse>,
    @SerialName("page")
    val page: Int,
    @SerialName("total_pages")
    val totalPages: Int
) 