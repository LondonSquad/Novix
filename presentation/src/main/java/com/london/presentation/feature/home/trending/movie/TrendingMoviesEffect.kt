package com.london.presentation.feature.home.trending.movie

sealed interface TrendingMoviesEffect {
    data object NavigateBack : TrendingMoviesEffect
    data class NavigateToMovie(val movieId: Int) : TrendingMoviesEffect
}
