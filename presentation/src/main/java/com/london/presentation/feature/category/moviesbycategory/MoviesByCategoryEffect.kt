package com.london.presentation.feature.category.moviesbycategory

sealed interface MoviesByCategoryEffect {

    data object NavigateBack : MoviesByCategoryEffect
    data class NavigateToMovieDetails(val movieId: Int) : MoviesByCategoryEffect
}
