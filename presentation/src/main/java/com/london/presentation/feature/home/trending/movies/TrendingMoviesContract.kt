package com.london.presentation.feature.home.trending.movies

import androidx.paging.LoadState
import com.london.presentation.utils.MovieGenre

interface TrendingMoviesContract {
    fun onMovieClick(id: Int)
    fun onGenreSelected(genre: MovieGenre)
    fun onBack()
    fun onRetry()
    fun onError(error: LoadState.Error)
}

fun defaultTrendingMoviesContract() = object : TrendingMoviesContract {
    override fun onMovieClick(id: Int) {}
    override fun onGenreSelected(genre: MovieGenre) {}
    override fun onBack() {}
    override fun onRetry() {}
    override fun onError(error: LoadState.Error) {}
}