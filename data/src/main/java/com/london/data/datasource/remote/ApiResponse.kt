package com.london.data.datasource.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    @SerialName("page")
    val currentPage: Int,
    @SerialName("results")
    val items: List<T>,
    @SerialName("total_pages")
    val totalPages: Int,
    @SerialName("total_results")
    val totalItems: Int
)