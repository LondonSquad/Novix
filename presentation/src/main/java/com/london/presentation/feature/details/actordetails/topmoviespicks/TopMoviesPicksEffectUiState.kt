package com.london.presentation.feature.details.actordetails.topmoviespicks

sealed interface TopMoviesPicksEffectUiState {
    data object NavigateBack : TopMoviesPicksEffectUiState
    data class NavigationToMovieDetails(val movieId: Int) : TopMoviesPicksEffectUiState
}