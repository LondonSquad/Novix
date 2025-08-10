package com.london.presentation.feature.home.continuewatching

import com.london.presentation.shared.MediaCategory
import com.london.presentation.utils.MovieGenre
import com.london.presentation.utils.TvShowGenre

interface ContinueWatchingContract {
    fun onBack()
    fun onRetry()
    fun onNavigateToMovie(id: Int)
    fun onNavigateToTvShow(id: Int)
    fun onMovieGenreChanged(genre: MovieGenre)
    fun onTvShowGenreChanged(genre: TvShowGenre)
    fun onMediaCategoryTabSelected(selectedMediaCategory: MediaCategory)
}

fun defaultContinueWatchingContract() = object : ContinueWatchingContract {
    override fun onBack() {}
    override fun onRetry() {}
    override fun onNavigateToMovie(id: Int) {}
    override fun onNavigateToTvShow(id: Int) {}
    override fun onMovieGenreChanged(genre: MovieGenre) {}
    override fun onTvShowGenreChanged(genre: TvShowGenre) {}
    override fun onMediaCategoryTabSelected(selectedMediaCategory: MediaCategory) {}
}