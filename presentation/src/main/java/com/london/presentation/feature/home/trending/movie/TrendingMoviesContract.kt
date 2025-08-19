package com.london.presentation.feature.home.trending.movie

import com.london.presentation.shared.genre.MovieGenreUi

interface TrendingMoviesContract {
    fun onBackClick()
    fun onRetryClick()
    fun onMovieClick(id: Int)
    fun onGenreClick(genre: MovieGenreUi)
}

fun defaultTrendingMoviesContract() = object : TrendingMoviesContract {
    override fun onBackClick() {}
    override fun onRetryClick() {}
    override fun onMovieClick(id: Int) {}
    override fun onGenreClick(genre: MovieGenreUi) {}
}
