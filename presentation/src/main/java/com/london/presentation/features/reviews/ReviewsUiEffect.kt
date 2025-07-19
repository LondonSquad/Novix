package com.london.presentation.features.reviews


sealed interface ReviewsUiEffect {
    data object OnNavigateBack : ReviewsUiEffect
    data class OnNavigateToReviewDetails(val reviewId: Int) : ReviewsUiEffect
}
