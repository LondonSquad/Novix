package com.london.presentation.feature.category.moviesbycategory

interface MoviesByCategoryContract {

    fun onBack()
    fun onSavedClick(movieId: Int)
    fun onMovieClick(movieId: Int)
}
