package com.london.presentation.feature.details.actor.info.gallery

import com.london.presentation.shared.base.ErrorState

data class ActorsGalleryUiState(
    val error: ErrorState? = null,
    val isLoading: Boolean = false,
    val images: List<String> = emptyList(),
)
