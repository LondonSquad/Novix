package com.london.presentation.screen.category.moviesbycategory

interface MoviesByCategoryContract {
    fun onSavedClick(movieId: Int)
    fun onMovieClick(movieId: Int)
    fun onBackClick()
}