package com.london.presentation.feature.home

import androidx.paging.PagingData
import com.london.domain.entity.UpComingMovie
import com.london.presentation.feature.home.popular.PopularUiMedia
import com.london.presentation.shared.base.ErrorState
import com.london.presentation.utils.MovieGenre
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow

data class HomeScreenUiState(
    val error: ErrorState? = null,
    val isLoading: Boolean = false,
    val isPopularLoading: Boolean = false,
    val isTopRatedLoading: Boolean = false,
    val selectedMovieGenre: MovieGenre = MovieGenre.All,
    val topRatedMediaList: List<HomeUiMedia> = emptyList(),
    val popularMediaList: List<PopularUiMedia> = emptyList(),
    val recentWatchedMediaFlow: Flow<List<HomeUiMedia>> = emptyFlow(),
    val movieGenres: List<MovieGenre> = MovieGenre.entries.toList(),
    val upcomingMovies: Flow<PagingData<UpComingMovie>> = emptyFlow(),
    val selectedCategoryFlow: MutableStateFlow<Int?> = MutableStateFlow(null)
)
