package com.london.presentation.feature.details.actor

sealed interface ActorEffectUiState {
    data object NavigationBack : ActorEffectUiState
    data class NavigateToGallery(val actorId: Int) : ActorEffectUiState
    data class NavigateToTvShowPicks(val actorId: Int) : ActorEffectUiState
    data class NavigateToMovieScreen(val movieId: Int) : ActorEffectUiState
    data class NavigateToMoviePicks(val actorId: Int) : ActorEffectUiState
    data class NavigateToTvShowScreen(val tvShowId: Int) : ActorEffectUiState
}