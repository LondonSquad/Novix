package com.london.presentation.feature.home

import androidx.paging.PagingData
import com.london.domain.entity.Movie
import com.london.domain.entity.popular.PopularMovie
import com.london.domain.entity.popular.PopularTvShow
import com.london.presentation.feature.base.ErrorState
import com.london.presentation.utils.MovieGenre
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class HomeScreenUiState(
    val isLoading: Boolean = false,
    val error: ErrorState? = null,
    val isTopRatedLoading: Boolean = false,
    val popularMediaList: List<PopularUiMedia> = emptyList(),
    val topRatedMediaList: List<HomeUiMedia> = emptyList(),
    val recentWatchedMediaList: List<HomeUiMedia> = emptyList(),
    val upcomingMovies: StateFlow<PagingData<Movie>> = MutableStateFlow(PagingData.empty()),
    val selectedMovieGenre: MovieGenre = MovieGenre.All,
    val movieGenres: List<MovieGenre> = MovieGenre.entries.toList(),
)
