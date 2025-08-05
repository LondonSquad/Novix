package com.london.presentation.feature.toprated

import com.london.designsystem.component.MediaCategory
import com.london.presentation.utils.MovieGenre
import com.london.presentation.utils.TvShowGenre

interface TopRatedContract {
    fun movieGenre(genre: MovieGenre)
    fun tvShowGenre(genre: TvShowGenre)
    fun onMediaCategoryTabSelected(selectedMediaCategory: MediaCategory)
    fun onBackClicked()
    fun onMovieClick(id: Int)
    fun onTvShowClick(id: Int)
    fun onRetry()
}