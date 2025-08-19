package com.london.presentation.feature.home

import androidx.paging.PagingData
import com.london.domain.entity.movie.UpComingMovie
import com.london.presentation.feature.home.popular.PopularUiMedia
import com.london.presentation.shared.base.ErrorState
import com.london.presentation.shared.genre.MovieGenreUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow

data class HomeScreenUiState(
    val error: ErrorState? = null,
    val isLoading: Boolean = false,
    val bookmarkedMovieId: Int = 0,
    val isPopularLoading: Boolean = false,
    val isTopRatedLoading: Boolean = false,
    val isBookmarkSheetVisible: Boolean = false,
    val topRatedMediaList: List<HomeUiMedia> = emptyList(),
    val selectedMovieGenre: MovieGenreUi = MovieGenreUi.All,
    val popularMediaList: List<PopularUiMedia> = emptyList(),
    val movieGenres: List<MovieGenreUi> = MovieGenreUi.getList(),
    val upcomingMovies: Flow<PagingData<UpComingMovie>> = emptyFlow(),
    val recentWatchedMediaFlow: Flow<List<HomeUiMedia>> = emptyFlow(),
    val selectedCategoryFlow: MutableStateFlow<MovieGenreUi?> = MutableStateFlow(null)
)
