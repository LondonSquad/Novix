package com.london.presentation.feature.home.continuewatching

import com.london.presentation.shared.MediaCategory
import com.london.presentation.shared.genre.MovieGenreUi
import com.london.presentation.shared.genre.TvShowGenreUi

interface ContinueWatchingContract {
    fun onBackClick()
    fun onRetryClick()
    fun onNavigateToMovieClick(id: Int)
    fun onNavigateToTvShowClick(id: Int)
    fun onMovieGenreClick(genre: MovieGenreUi)
    fun onTvShowGenreClick(genre: TvShowGenreUi)
    fun onMediaCategoryTabClick(selectedMediaCategory: MediaCategory)
    fun onBookmarkSheetDismiss()
    fun onManageBookmarkClicked(movieId: Int)
}
