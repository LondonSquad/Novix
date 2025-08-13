package com.london.presentation.feature.category.movie

sealed interface MovieCategoryEffect {

    data object NavigationBack : MovieCategoryEffect
    data class MovieDetailsNavigation(val movieId: Int) : MovieCategoryEffect
}
