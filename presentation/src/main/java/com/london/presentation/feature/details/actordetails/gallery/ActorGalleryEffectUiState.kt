package com.london.presentation.feature.details.actordetails.gallery

sealed interface ActorGalleryEffectUiState {
    data object NavigationBack : ActorGalleryEffectUiState
}