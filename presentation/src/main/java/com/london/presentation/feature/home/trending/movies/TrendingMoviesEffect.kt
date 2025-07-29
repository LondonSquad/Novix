package com.london.presentation.feature.home.trending.movies

sealed interface TrendingMoviesEffect {
    data class NavigateToMovie(val movieId: Int) : TrendingMoviesEffect
    data object NavigateBack : TrendingMoviesEffect
}
