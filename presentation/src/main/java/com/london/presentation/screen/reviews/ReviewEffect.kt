package com.london.presentation.screen.reviews

sealed interface ReviewEffect {
    data object NavigateBack : ReviewEffect
}