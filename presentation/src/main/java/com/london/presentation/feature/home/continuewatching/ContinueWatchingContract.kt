package com.london.presentation.feature.home.continuewatching

import com.london.presentation.shared.MediaCategory
import com.london.presentation.shared.genre.MovieGenreUi
import com.london.presentation.shared.genre.TvShowGenreUi

interface ContinueWatchingContract {
    fun onBackClick()
    fun onRetryClick()
    fun onNavigateToMovie(id: Int)
    fun onNavigateToTvShow(id: Int)
    fun onMovieGenreClick(genre: MovieGenreUi)
    fun onTvShowGenreClick(genre: TvShowGenreUi)
    fun onMediaCategoryTabClick(selectedMediaCategory: MediaCategory)
}

fun defaultContinueWatchingContract() = object : ContinueWatchingContract {
    override fun onBackClick() {}
    override fun onRetryClick() {}
    override fun onNavigateToMovie(id: Int) {}
    override fun onNavigateToTvShow(id: Int) {}
    override fun onMovieGenreClick(genre: MovieGenreUi) {}
    override fun onTvShowGenreClick(genre: TvShowGenreUi) {}
    override fun onMediaCategoryTabClick(selectedMediaCategory: MediaCategory) {}
}