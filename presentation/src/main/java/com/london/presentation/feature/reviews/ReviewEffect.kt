package com.london.presentation.feature.reviews

sealed interface ReviewEffect {
    data object NavigateBack : ReviewEffect
}