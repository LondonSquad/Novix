package com.london.presentation.feature.home.toprated

import androidx.paging.PagingData
import com.london.domain.entity.toprated.TopRatedMedia
import com.london.presentation.shared.MediaCategory
import com.london.presentation.utils.MovieGenre
import com.london.presentation.utils.TvShowGenre
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class TopRatedUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isMovieSelected: Boolean = true,
    val selectedMovieGenre: MovieGenre = MovieGenre.All,
    val selectedTvShowGenre: TvShowGenre = TvShowGenre.All,
    val movies: Flow<PagingData<TopRatedMedia>> = emptyFlow(),
    val tvSeries: Flow<PagingData<TopRatedMedia>> = emptyFlow(),
    val selectedMediaCategory: MediaCategory = MediaCategory.MOVIES
)
