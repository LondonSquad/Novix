package com.london.presentation.feature.account.rating

interface MyRatingsContract {
    fun onRetryClick()
    fun onBackClick()
    fun onItemClick(id: Int)
    fun onMovieClick(id: Int)
    fun onTvShowClick(id: Int)
    fun onDeleteMovieClick(id: Int)
    fun onDeleteTVShowClick(id: Int)
    fun onRatingCategorySelected(category: RatingCategory)
}
