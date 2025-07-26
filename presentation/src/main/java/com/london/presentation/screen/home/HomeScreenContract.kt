package com.london.presentation.screen.home

import com.london.presentation.utils.MovieGenre

interface HomeScreenContract {
    fun onMovieClick(id: Int)
    fun onTvShowClick(id: Int)
    fun onTrendingCardClicked(id: Int)
    fun onMovieGenreSelect(genre: MovieGenre)
}
