package com.london.presentation.feature.home

import com.london.presentation.shared.genre.MovieGenreUi

interface HomeScreenContract {
    fun onRetryClick()
    fun onTopRatedClick()
    fun onMovieClick(id: Int)
    fun onTvShowClick(id: Int)
    fun onContinueWatchingClick()
    fun onTrendingMoviesCardClick()
    fun onTrendingActorsCardClick()
    fun onTrendingTvShowsCardClick()
    fun onMovieGenreSelect(genre: MovieGenreUi)
    fun loadUpcomingMoviesClick(categoryId: Int?)
    fun onManageBookmarkClicked(movieId: Int)
    fun onBookmarkSheetDismiss()
}