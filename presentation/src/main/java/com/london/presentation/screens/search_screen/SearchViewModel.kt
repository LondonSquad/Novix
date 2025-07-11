package com.london.presentation.screens.search_screen

import androidx.lifecycle.ViewModel
import com.london.presentation.screens.search_screen.model.MovieUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SearchViewModel : ViewModel(), SearchInteractions {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    fun isMovieSaved(movie: MovieUi) = _uiState.value.savedMovies.contains(movie.id)

    override fun onSearchQueryChange(query: String) {
        TODO("Not yet implemented")
    }

    override fun onSearchFilterClick() {
        TODO("Not yet implemented")
    }

    override fun onCategorySelected(category: SearchCategory) {
        TODO("Not yet implemented")
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

}