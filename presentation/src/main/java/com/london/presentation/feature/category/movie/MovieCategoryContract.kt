package com.london.presentation.feature.category.movie

interface MovieCategoryContract {
    fun onBackClick()
    fun onMovieClick(movieId: Int)
    fun onBookmarkSheetDismiss()
    fun onManageBookmarkClick(movieId: Int)
}
