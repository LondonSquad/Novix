package com.london.data


data class ApiReviewResponse<T>(
    val id: Int,
    val page: Int,
    val results: List<T>,
    val total_pages: Int,
    val total_results: Int
)