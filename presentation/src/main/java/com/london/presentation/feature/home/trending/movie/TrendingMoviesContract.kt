package com.london.presentation.feature.home.trending.movie

import com.london.presentation.utils.MovieGenre

interface TrendingMoviesContract {
    fun onBackClick()
    fun onRetryClick()
    fun onMovieClick(id: Int)
    fun onGenreClick(genre: MovieGenre)
}

fun defaultTrendingMoviesContract() = object : TrendingMoviesContract {
    override fun onBackClick() {}
    override fun onRetryClick() {}
    override fun onMovieClick(id: Int) {}
    override fun onGenreClick(genre: MovieGenre) {}
}