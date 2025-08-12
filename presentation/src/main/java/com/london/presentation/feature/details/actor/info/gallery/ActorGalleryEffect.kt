package com.london.presentation.feature.details.actor.info.gallery

sealed interface ActorGalleryEffect {
    data object NavigateBack : ActorGalleryEffect
}
