package com.london.domain.entity


data class PagedFetchResponse<T>(
    val currentPage: Int = 0,
    val items: List<T> = emptyList(),
    val totalPages: Int = 0,
    val totalItems: Int = 0
)
