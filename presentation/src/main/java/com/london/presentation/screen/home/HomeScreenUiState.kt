package com.london.presentation.screen.home

import androidx.paging.PagingData
import com.london.domain.entity.Movie
import com.london.domain.entity.popular.PopularMovie
import com.london.domain.entity.popular.PopularTvShow
import com.london.presentation.screen.base.ErrorState
import com.london.presentation.utils.Genre
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class HomeScreenUiState(
    val isLoading: Boolean = false,
    val error: ErrorState? = null,
    val popularMovies: List<PopularMovie> = emptyList(),
    val popularTvShows: List<PopularTvShow> = emptyList(),
    val upcomingMovies:  StateFlow<PagingData<Movie>> = MutableStateFlow<PagingData<Movie>>(PagingData.empty()),
    val selectedGenre: Genre = Genre.All
)
