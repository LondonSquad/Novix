package com.london.presentation.feature.home.trending.movie

import com.london.presentation.shared.genre.MovieGenreUi

interface TrendingMoviesContract {
    fun onBackClick()
    fun onRetryClick()
    fun onMovieClick(id: Int)
    fun onGenreClick(genre: MovieGenreUi)
}
