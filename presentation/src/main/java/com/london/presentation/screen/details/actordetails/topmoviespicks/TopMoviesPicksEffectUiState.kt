package com.london.presentation.screen.details.actordetails.topmoviespicks

sealed interface TopMoviesPicksEffectUiState {
    data object NavigateBack : TopMoviesPicksEffectUiState
    data class NavigationToMovieDetails(val movieId: Int) : TopMoviesPicksEffectUiState
}