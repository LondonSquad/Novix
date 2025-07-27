package com.london.presentation.feature.category.moviesbycategory

sealed interface MoviesByCategoryEffect {
    data class NavigateToMovieDetails(val movieId: Int) : MoviesByCategoryEffect
    object NavigateBack : MoviesByCategoryEffect
}