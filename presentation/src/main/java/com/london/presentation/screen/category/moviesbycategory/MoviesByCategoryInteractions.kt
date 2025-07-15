package com.london.presentation.screen.category.moviesbycategory

interface MoviesByCategoryInteractions {
    fun onMovieClick(movieId: Int)
    fun onBackClick()
    fun onSavedClick(movieId: Int)
}