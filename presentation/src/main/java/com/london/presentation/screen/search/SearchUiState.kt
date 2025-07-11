package com.london.presentation.screen.search

data class SearchUiState(
    val searchQuery: String = "",
    val recentViewed: List<String> = emptyList(),
    val recentSearches: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val searchResults: List<SearchResult> = emptyList(),
    val isSearching: Boolean = false
)


data class SearchResult(
    val id: String,
    val title: String,
    val imageUrl: String,
    val rating: Float? = null,
    val year: String? = null
)