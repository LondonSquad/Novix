package com.london.presentation.feature.continuewatching

import com.london.designsystem.component.MediaCategory
import com.london.presentation.utils.MovieGenre
import com.london.presentation.utils.TvShowGenre

interface ContinueWatchingContract {
    fun onMovieGenreChanged(genre: MovieGenre)
    fun onTvShowGenreChanged(genre: TvShowGenre)
    fun onMediaCategoryTabSelected(selectedMediaCategory: MediaCategory)
    fun onBack()
    fun onNavigateToMovie(id: Int)
    fun onNavigateToTvShow(id: Int)
}

fun defaultContinueWatchingContract() = object : ContinueWatchingContract {
    override fun onMovieGenreChanged(genre: MovieGenre) {}
    override fun onTvShowGenreChanged(genre: TvShowGenre) {}
    override fun onMediaCategoryTabSelected(selectedMediaCategory: MediaCategory) {}
    override fun onBack() {}
    override fun onNavigateToMovie(id: Int) {}
    override fun onNavigateToTvShow(id: Int) {}
}