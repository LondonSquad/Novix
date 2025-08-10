package com.london.presentation.feature.home

import com.london.presentation.utils.MovieGenre

interface HomeScreenContract {
    fun onRetry()
    fun onTopRatedClick()
    fun onMovieClick(id: Int)
    fun onTvShowClick(id: Int)
    fun onContinueWatchingClick()
    fun onTrendingMoviesCardClicked()
    fun onTrendingActorsCardClicked()
    fun onTrendingTvShowsCardClicked()
    fun onMovieGenreSelect(genre: MovieGenre)
}

fun defaultHomeScreenContract() = object : HomeScreenContract {
    override fun onRetry() {}
    override fun onTopRatedClick() {}
    override fun onMovieClick(id: Int) {}
    override fun onTvShowClick(id: Int) {}
    override fun onContinueWatchingClick() {}
    override fun onTrendingMoviesCardClicked() {}
    override fun onTrendingActorsCardClicked() {}
    override fun onTrendingTvShowsCardClicked() {}
    override fun onMovieGenreSelect(genre: MovieGenre) {}
}

