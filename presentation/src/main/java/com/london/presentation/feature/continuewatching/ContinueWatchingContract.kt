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

fun defaultContinueWatchingContract()= object : ContinueWatchingContract{
    override fun movieGenre(genre: MovieGenre) {}
    override fun tvShowGenre(genre: TvShowGenre) {}
    override fun tabSelected(index: Int) {}
    override fun onBack() {}
    override fun onMovieClick(id: Int){}
    override fun onTvShowClick(id: Int) {}
}