package com.london.presentation.feature.category.movie

sealed interface MovieCategoryEffect {

    data object NavigateBack : MovieCategoryEffect
    data class NavigateToMovieDetails(val movieId: Int) : MovieCategoryEffect
}
