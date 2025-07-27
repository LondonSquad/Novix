package com.london.presentation.feature.category.moviesbycategory

interface MoviesByCategoryContract {
    fun onSavedClick(movieId: Int)
    fun onMovieClick(movieId: Int)
    fun onBackClick()
}