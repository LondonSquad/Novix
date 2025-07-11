package com.london.presentation.screen.search

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        SearchUiState(
            recentViewed = listOf(
                "https://image.tmdb.org/t/p/w500/rktDFPbfHfUbArZ6OOOKsXcv0Bm.jpg",
                "https://image.tmdb.org/t/p/w500/8uO0gUM8aNqYLs1OsTBQiXu0fEv.jpg",
                "https://image.tmdb.org/t/p/w500/6FfCtAuVAW8XJjZ7eWeLibRLWTw.jpg"
            ),
            recentSearches = listOf(
                "Shutter island",
                "Seven",
                "Th",
                "The prestige",
                "Spider man"
            )
        )
    )
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    fun onSearchQueryChange(newValue: TextFieldValue) {
        _uiState.value = _uiState.value.copy(searchQuery = newValue)

        val query = newValue.text
        if (query.isNotEmpty() && query.length >= 2) {
            performSearch(query)
        } else {
            _uiState.value = _uiState.value.copy(
                searchResults = emptyList(),
                isSearching = false
            )
        }
    }

    fun onSearchSubmit() {
        val query = _uiState.value.searchQuery.text.trim()
        if (query.isNotEmpty()) {
            addToRecentSearches(query)
            performSearch(query)
        }
    }

    private fun performSearch(query: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSearching = true)

            try {
                val mockResults = generateMockSearchResults(query)

                _uiState.value = _uiState.value.copy(
                    searchResults = mockResults,
                    isSearching = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    searchResults = emptyList(),
                    isSearching = false
                )
            }
        }
    }

    private fun generateMockSearchResults(query: String): List<SearchResult> {
        return listOf(
            SearchResult(
                id = "1",
                title = "Movie Result for: $query",
                imageUrl = "https://image.tmdb.org/t/p/w500/rktDFPbfHfUbArZ6OOOKsXcv0Bm.jpg",
                rating = 8.5f,
                year = "2023"
            ),
            SearchResult(
                id = "2",
                title = "Another Result: $query",
                imageUrl = "https://image.tmdb.org/t/p/w500/8uO0gUM8aNqYLs1OsTBQiXu0fEv.jpg",
                rating = 7.2f,
                year = "2022"
            )
        )
    }

    private fun addToRecentSearches(query: String) {
        val currentSearches = _uiState.value.recentSearches.toMutableList()

        currentSearches.remove(query)
        currentSearches.add(0, query)

        if (currentSearches.size > 10) {
            currentSearches.removeAt(currentSearches.size - 1)
        }

        _uiState.value = _uiState.value.copy(recentSearches = currentSearches)
    }

    fun clearRecentViewed() {
        _uiState.value = _uiState.value.copy(recentViewed = emptyList())
    }

    fun clearRecentSearches() {
        _uiState.value = _uiState.value.copy(recentSearches = emptyList())
    }

    fun removeRecentSearch(search: String) {
        val updatedSearches = _uiState.value.recentSearches.filter { it != search }
        _uiState.value = _uiState.value.copy(recentSearches = updatedSearches)
    }

    fun onRecentSearchClick(search: String) {
        _uiState.value = _uiState.value.copy(searchQuery = TextFieldValue(search))
        performSearch(search)
    }

    fun clearSearch() {
        _uiState.value = _uiState.value.copy(
            searchQuery = TextFieldValue(""),
            searchResults = emptyList(),
            isSearching = false
        )
    }
}