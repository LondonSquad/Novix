package com.london.presentation.feature.category.movie

interface MovieCategoryContract {

    fun onBackClick()
    fun onSavedClick(movieId: Int)
    fun onMovieClick(movieId: Int)
}
