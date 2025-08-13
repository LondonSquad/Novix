package com.london.presentation.feature.details.actor.info.gallery

sealed interface ActorsGalleryEffect {
    data object BackNavigation : ActorsGalleryEffect
}
