package com.london.presentation.feature.home.trending.movie

sealed interface TrendingMoviesEffect {
    data object OnNavigateBack : TrendingMoviesEffect
    data class OnNavigateToMovieClick(val movieId: Int) : TrendingMoviesEffect
}
