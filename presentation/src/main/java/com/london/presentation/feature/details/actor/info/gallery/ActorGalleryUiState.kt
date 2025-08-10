package com.london.presentation.feature.details.actor.info.gallery

import com.london.presentation.shared.base.ErrorState

data class ActorGalleryUiState(
    val images: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val error: ErrorState? = null
)