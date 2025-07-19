package com.london.presentation.features.details.actordetails.gallery

data class ActorGalleryUiState(
    val images: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)