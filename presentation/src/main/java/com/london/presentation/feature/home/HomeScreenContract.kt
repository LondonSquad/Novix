package com.london.presentation.feature.home

import com.london.presentation.shared.genre.MovieGenreUi

interface HomeScreenContract {
    fun onRetryClick()
    fun onTopRatedClick()
    fun onMovieClick(id: Int)
    fun onTvShowClick(id: Int)
    fun onBookmarkSheetDismiss()
    fun onContinueWatchingClick()
    fun onTrendingMoviesCardClick()
    fun onTrendingActorsCardClick()
    fun onTrendingTvShowsCardClick()
    fun onManageBookmarkClick(movieId: Int)
    fun onMovieGenreSelect(genre: MovieGenreUi)
    fun loadUpcomingMoviesClick(genre: MovieGenreUi)
}