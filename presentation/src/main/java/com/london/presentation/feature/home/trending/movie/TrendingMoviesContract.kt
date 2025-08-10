package com.london.presentation.feature.home.trending.movie

import com.london.presentation.utils.MovieGenre

interface TrendingMoviesContract {
    fun onBack()
    fun onRetry()
    fun onMovieClick(id: Int)
    fun onGenreSelected(genre: MovieGenre)
}

fun defaultTrendingMoviesContract() = object : TrendingMoviesContract {
    override fun onBack() {}
    override fun onRetry() {}
    override fun onMovieClick(id: Int) {}
    override fun onGenreSelected(genre: MovieGenre) {}
}