package com.london.presentation.screen.home.trending

sealed interface TrendingMoviesEffect {
    data class NavigateToMovie(val movieId: Int) : TrendingMoviesEffect
    data object NavigateBack : TrendingMoviesEffect
} 