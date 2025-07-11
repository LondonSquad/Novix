package com.london.presentation.screen.search

import androidx.compose.ui.text.input.TextFieldValue

data class SearchUiState(
    val searchQuery: TextFieldValue = TextFieldValue(""),
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