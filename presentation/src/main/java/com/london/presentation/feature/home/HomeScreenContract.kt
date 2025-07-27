package com.london.presentation.feature.home

import com.london.presentation.utils.MovieGenre

interface HomeScreenContract {
    fun onMovieClick(id: Int)
    fun onTvShowClick(id: Int)
    fun onGenreSelect(genre: MovieGenre)
    fun onTopRatedClick()
}
