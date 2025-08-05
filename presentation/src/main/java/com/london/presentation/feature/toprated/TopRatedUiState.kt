package com.london.presentation.feature.toprated

import androidx.paging.PagingData
import com.london.designsystem.component.MediaCategory
import com.london.domain.entity.toprated.TopRatedMovie
import com.london.domain.entity.toprated.TopRatedTvSeries
import com.london.presentation.utils.MovieGenre
import com.london.presentation.utils.TvShowGenre
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class TopRatedUiState(
    val movies: Flow<PagingData<TopRatedMovie>> = emptyFlow(),
    val tvSeries: Flow<PagingData<TopRatedTvSeries>> = emptyFlow(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val selectedMediaCategory: MediaCategory = MediaCategory.MOVIES,
    val selectedMovieGenre: MovieGenre = MovieGenre.All,
    val selectedTvShowGenre: TvShowGenre = TvShowGenre.All,
    val isMovieSelected: Boolean = true
)


