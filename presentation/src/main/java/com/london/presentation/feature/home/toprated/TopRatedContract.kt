package com.london.presentation.feature.home.toprated

import com.london.presentation.shared.MediaCategory
import com.london.presentation.utils.MovieGenre
import com.london.presentation.utils.TvShowGenre

interface TopRatedContract {
    fun onRetry()
    fun onBackClicked()
    fun onMovieClick(id: Int)
    fun onTvShowClick(id: Int)
    fun movieGenre(genre: MovieGenre)
    fun tvShowGenre(genre: TvShowGenre)
    fun onMediaCategoryTabSelected(selectedMediaCategory: MediaCategory)
}