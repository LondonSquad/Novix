package com.london.presentation.feature.home.toprated

import androidx.paging.PagingData
import com.london.domain.entity.toprated.TopRatedMedia
import com.london.presentation.shared.MediaCategory
import com.london.presentation.shared.genre.MovieGenreUi
import com.london.presentation.shared.genre.TvShowGenreUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class TopRatedUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isMovieSelected: Boolean = true,
    val selectedMovieGenre: MovieGenreUi = MovieGenreUi.All,
    val movies: Flow<PagingData<TopRatedMedia>> = emptyFlow(),
    val selectedTvShowGenre: TvShowGenreUi = TvShowGenreUi.All,
    val tvSeries: Flow<PagingData<TopRatedMedia>> = emptyFlow(),
    val selectedMediaCategory: MediaCategory = MediaCategory.Movies,
    val isBookmarkSheetVisible: Boolean = false,
    val bookmarkedMovieId: Int = 0,
)
