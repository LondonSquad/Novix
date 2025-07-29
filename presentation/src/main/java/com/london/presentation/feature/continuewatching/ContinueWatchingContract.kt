package com.london.presentation.feature.continuewatching

import com.london.presentation.utils.MovieGenre
import com.london.presentation.utils.TvShowGenre

interface ContinueWatchingContract {
    fun movieGenre(genre: MovieGenre)
    fun tvShowGenre(genre: TvShowGenre)
    fun tabSelected(index: Int)
    fun onBack()
    fun onMovieClick(id: Int)
    fun onTvShowClick(id: Int)
}