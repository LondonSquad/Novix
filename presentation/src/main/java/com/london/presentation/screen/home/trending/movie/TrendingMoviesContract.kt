package com.london.presentation.screen.home.trending.movie

import com.london.presentation.utils.MovieGenre

interface TrendingMoviesContract {
    fun onMovieClick(id: Int)
    fun onBackClick()
    fun onGenreSelected(genre: MovieGenre)
}

fun defaultTrendingMoviesContract() = object : TrendingMoviesContract {
    override fun onMovieClick(id: Int) {}
    override fun onBackClick() {}
    override fun onGenreSelected(genre: MovieGenre) {}
}