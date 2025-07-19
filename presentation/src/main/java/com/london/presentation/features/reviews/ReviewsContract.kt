package com.london.presentation.features.reviews


interface ReviewsContract {
    fun navigateBack()
    fun showReviewDetails(reviewId: Int)
}


fun defaultReviewsContract() = object : ReviewsContract {
    override fun navigateBack() = Unit
    override fun showReviewDetails(reviewId: Int) = Unit
}
