package com.london.presentation.feature.details.actor.info.gallery

sealed interface ActorGalleryEffectUiState {
    data object NavigationBack : ActorGalleryEffectUiState
}