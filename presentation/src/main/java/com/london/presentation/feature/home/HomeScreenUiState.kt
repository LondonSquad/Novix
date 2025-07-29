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
    val popularMovies: List<PopularMovie> = emptyList(),
    val popularTvShows: List<PopularTvShow> = emptyList(),
    val topRatedUiMediaList: List<HomeUiMedia> = emptyList(),
    val recentWatchedMediaList: List<HomeUiMedia> = emptyList(),
    val upcomingMovies: StateFlow<PagingData<Movie>> = MutableStateFlow(PagingData.empty()),
    val selectedGenre: MovieGenre = MovieGenre.All
)
