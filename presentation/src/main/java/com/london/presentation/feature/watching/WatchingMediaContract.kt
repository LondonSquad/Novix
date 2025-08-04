package com.london.presentation.feature.watching

import com.london.presentation.utils.MovieGenre
import com.london.presentation.utils.TvShowGenre

interface WatchingMediaContract {
    fun movieGenre(genre: MovieGenre)
    fun tvShowGenre(genre: TvShowGenre)
    fun tabSelected(index: Int)
    fun onBack()
    fun onNavigateToMovie(id: Int)
    fun onNavigateToTvShow(id: Int)
}

fun defaultWatchingMediaContract () = object : WatchingMediaContract {
    override fun movieGenre(genre: MovieGenre) {}
    override fun tvShowGenre(genre: TvShowGenre) {}
    override fun tabSelected(index: Int) {}
    override fun onBack() {}
    override fun onNavigateToMovie(id: Int) {}
    override fun onNavigateToTvShow(id: Int) {}
}