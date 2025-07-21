package com.london.presentation.screen.details.actordetails.gallery

sealed interface ActorGalleryEffectUiState {
    data object NavigationBack : ActorGalleryEffectUiState
}