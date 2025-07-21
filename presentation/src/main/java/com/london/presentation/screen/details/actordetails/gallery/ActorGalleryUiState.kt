package com.london.presentation.screen.details.actordetails.gallery

import com.london.presentation.screen.base.ErrorState

data class ActorGalleryUiState(
    val images: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val error: ErrorState? = null
)