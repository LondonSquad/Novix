package com.london.presentation.screen.search

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.london.presentation.screen.search.model.MovieUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel(), SearchInteractions {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    fun isMovieSaved(movie: MovieUi) = _uiState.value.savedMovies.contains(movie.id)

    override fun onSearchQueryChange(newValue: TextFieldValue) {
        _uiState.update {
            it.copy(searchQuery = newValue)
        }
    }

    override fun onSearchFilterClick() {
        TODO("Not yet implemented")
    }

    override fun onCategorySelected(category: SearchCategory) {
        _uiState.update {
            it.copy(selectedCategory = category)
        }
    }

    override fun onSavedMovieClick(movie: MovieUi) {
        _uiState.update { currentState ->
            val isNowSaved = !currentState.savedMovies.contains(movie.id)
            val updatedSavedMovies = if (isNowSaved) {
                currentState.savedMovies + movie.id
            } else {
                currentState.savedMovies - movie.id
            }

            val updatedMovies = currentState.movieResults.map {
                if (it.id == movie.id) it.copy(isSaved = isNowSaved) else it
            }

            currentState.copy(
                savedMovies = updatedSavedMovies,
                movieResults = updatedMovies
            )
        }
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
    }

    fun clearSearch() {
        _uiState.value = _uiState.value.copy(
            searchQuery = TextFieldValue(""),
        )
    }
}