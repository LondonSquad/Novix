package com.london.presentation.screen.home

import androidx.paging.PagingData
import com.london.domain.entity.Movie
import com.london.domain.entity.popular.PopularMovie
import com.london.presentation.screen.base.ErrorState
import com.london.presentation.utils.Genre
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class HomeScreenUiState(
    val isLoading: Boolean = false,
    val error: ErrorState? = null,
    val popularMovies: List<PopularMovie> = emptyList(),
    val upcomingMovies: Flow<PagingData<Movie>> = flow {},
    val selectedGenre: Genre = Genre.All
)
