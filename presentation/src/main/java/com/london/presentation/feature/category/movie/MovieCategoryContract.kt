package com.london.presentation.feature.category.movie

interface MovieCategoryContract {
    fun onBack()
    fun onSavedClick(movieId: Int)
    fun onMovieClick(movieId: Int)
}
