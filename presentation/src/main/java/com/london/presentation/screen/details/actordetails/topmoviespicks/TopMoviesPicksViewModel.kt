package com.london.presentation.screen.details.actordetails.topmoviespicks

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TopMoviesPicksViewModel : ViewModel(), TopMoviesPicksInteractions {
    private val _uiState = MutableStateFlow(TopMoviesPicksUiState())
    val uiState: StateFlow<TopMoviesPicksUiState> = _uiState.asStateFlow()
    override fun onMovieClick(movieId: Int) {
        //navigate to movie details
    }

    override fun onBackClick() {
        //navigate back
    }

    override fun onSavedClick(movieId: Int) {
        //save movie
    }
}