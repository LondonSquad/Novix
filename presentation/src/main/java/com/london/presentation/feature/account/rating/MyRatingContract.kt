package com.london.presentation.feature.account.rating

interface MyRatingContract {
    fun onBackClick()
    fun onRetryClick()
    fun onMovieClick(id: Int)
    fun onTvShowClick(id: Int)
    fun onDeleteMovieClick(id: Int)
    fun onDeleteTVShowClick(id: Int)
    fun onRatingCategorySelected(category: RatingCategory)
}
