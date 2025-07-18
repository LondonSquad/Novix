package com.london.presentation.screen.details.actordetails.gallery

data class ActorGalleryUiState(
    val images: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)