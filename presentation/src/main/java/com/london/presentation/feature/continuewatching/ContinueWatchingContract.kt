package com.london.presentation.feature.continuewatching

import com.london.presentation.utils.MovieGenre
import com.london.presentation.utils.TvShowGenre

interface ContinueWatchingContract {
    fun movieGenre(genre: MovieGenre)
    fun tvShowGenre(genre: TvShowGenre)
    fun tabSelected(index: Int)
    fun onBack()
    fun onNavigateToMovie(id: Int)
    fun onNavigateToTvShow(id: Int)
}

fun defaultContinueWatchingContract()= object : ContinueWatchingContract{
    override fun movieGenre(genre: MovieGenre) {}
    override fun tvShowGenre(genre: TvShowGenre) {}
    override fun tabSelected(index: Int) {}
    override fun onBack() {}
    override fun onNavigateToMovie(id: Int){}
    override fun onNavigateToTvShow(id: Int) {}
}