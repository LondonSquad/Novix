package com.london.domain.entity.shared

data class PagedFetchResponse<T>(
    val currentPage: Int,
    val items: List<T>,
    val totalPages: Int,
    val totalItems: Int
)