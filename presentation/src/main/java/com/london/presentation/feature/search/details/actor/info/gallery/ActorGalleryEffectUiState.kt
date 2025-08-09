package com.london.presentation.feature.search.details.actor.info.gallery

sealed interface ActorGalleryEffectUiState {
    data object NavigationBack : ActorGalleryEffectUiState
}