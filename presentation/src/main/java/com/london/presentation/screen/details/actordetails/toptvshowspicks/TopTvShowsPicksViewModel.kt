package com.london.presentation.screen.details.actordetails.toptvshowspicks

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TopTvShowsPicksViewModel : ViewModel(), TopTvShowsPicksInteractions {
    private val _uiState = MutableStateFlow(TopTvShowsPicksUiState())
    val uiState: StateFlow<TopTvShowsPicksUiState> = _uiState.asStateFlow()
    override fun onMovieClick(movieId: Int) {
        // TODO(navigate to movie details)
    }

    override fun onBackClick() {
        // TODO(navigate back)
    }

    override fun onSaveMovie(movieId: Int) {
        // TODO(save movie)
    }
}