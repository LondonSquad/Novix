package com.london.presentation.feature.home.toprated

import com.london.presentation.shared.MediaCategory
import com.london.presentation.shared.genre.MovieGenreUi
import com.london.presentation.shared.genre.TvShowGenreUi

interface TopRatedContract {
    fun onRetry()
    fun onBackClicked()
    fun onMovieClick(id: Int)
    fun onTvShowClick(id: Int)
    fun movieGenre(genre: MovieGenreUi)
    fun tvShowGenre(genre: TvShowGenreUi)
    fun onMediaCategoryTabSelected(selectedMediaCategory: MediaCategory)
}