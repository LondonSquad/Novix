package com.london.presentation.screen.category.moviesbycategory

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MoviesByCategoryViewModel : ViewModel(), MoviesByCategoryInteractions {
    private val _uiState = MutableStateFlow(MoviesByCategoryUiState())
    val uiState: StateFlow<MoviesByCategoryUiState> = _uiState.asStateFlow()
    override fun onMovieClick(movieId: Int) {

    }

    override fun onBackClick() {
    }

    override fun onSavedClick(movieId: Int) {
    }
}