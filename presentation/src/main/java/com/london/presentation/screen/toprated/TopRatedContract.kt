package com.london.presentation.screen.toprated

import com.london.presentation.utils.MovieGenre
import com.london.presentation.utils.TvShowGenre

interface TopRatedContract {
    fun movieGenreClicked(genre: MovieGenre)
    fun tvShowGenreClicked(genre: TvShowGenre)
    fun tabSelected(index: Int)
    fun onBackClicked()
    fun onMovieClick(id: Int)
    fun onTvShowClick(id: Int)
}
