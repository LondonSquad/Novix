package com.london.presentation.screen.category.moviesbycategory

sealed interface MoviesByCategoryEffect {
    data class NavigateToMovieDetails(val movieId: Int) : MoviesByCategoryEffect
    object NavigateBack : MoviesByCategoryEffect
}