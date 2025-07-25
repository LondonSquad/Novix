package com.london.presentation.screen.home

import com.london.presentation.utils.MovieGenre

interface HomeScreenContract {
    fun onMovieClick(id: Int)
    fun onTvShowClick(id: Int)
    fun onGenreSelect(genre: MovieGenre)
    fun onTopRatedClick()
}
