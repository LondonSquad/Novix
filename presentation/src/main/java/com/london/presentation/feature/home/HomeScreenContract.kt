package com.london.presentation.feature.home

import com.london.presentation.utils.MovieGenre

interface HomeScreenContract {
    fun onRetryClick()
    fun onTopRatedClick()
    fun onMovieClick(id: Int)
    fun onTvShowClick(id: Int)
    fun onContinueWatchingClick()
    fun onTrendingMoviesCardClick()
    fun onTrendingActorsCardClick()
    fun onTrendingTvShowsCardClick()
    fun onMovieGenreSelect(genre: MovieGenre)
    fun loadUpcomingMoviesClick(categoryId: Int?)
}