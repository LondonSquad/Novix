package com.london.presentation.feature.home

import com.london.presentation.utils.MovieGenre

interface HomeScreenContract {
    fun onMovieClick(id: Int)
    fun onTvShowClick(id: Int)
    fun onTopRatedClick()
    fun onContinueWatchingClick()
    fun onTrendingMoviesCardClicked()
    fun onTrendingTvShowsCardClicked()
    fun onTrendingActorsCardClicked()
    fun onMovieGenreSelect(genre: MovieGenre)
}

fun defaultHomeScreenContract() = object : HomeScreenContract {
    override fun onMovieClick(id: Int) {}
    override fun onTvShowClick(id: Int) {}
    override fun onTopRatedClick() {}
    override fun onContinueWatchingClick() {}
    override fun onTrendingMoviesCardClicked() {}
    override fun onTrendingTvShowsCardClicked() {}
    override fun onTrendingActorsCardClicked() {}
    override fun onMovieGenreSelect(genre: MovieGenre) {}
}
