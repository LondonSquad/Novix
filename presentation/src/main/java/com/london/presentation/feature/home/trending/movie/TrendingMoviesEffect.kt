package com.london.presentation.feature.home.trending.movie

sealed interface TrendingMoviesEffect {
    data object BackNavigation : TrendingMoviesEffect
    data class MovieDetailsNavigation(val movieId: Int) : TrendingMoviesEffect
}
