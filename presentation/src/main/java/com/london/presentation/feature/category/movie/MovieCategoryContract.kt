package com.london.presentation.feature.category.movie

interface MovieCategoryContract {
    fun onBack()
    fun onMovieClick(movieId: Int)
    fun onBookmarkSheetDismiss()
    fun onManageBookmarkClicked(movieId: Int)
}
