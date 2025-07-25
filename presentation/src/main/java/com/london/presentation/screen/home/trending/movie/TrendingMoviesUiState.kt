package com.london.presentation.screen.home.trending.movie

import com.london.domain.entity.trending.Trending
import com.london.presentation.screen.base.ErrorState
import com.london.presentation.utils.Genre
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class TrendingMoviesUiState(
    val trendingMovies: Flow<PagingData<Trending>> = flow {},
    val errorState: ErrorState? = null,
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val backdropPath: String = "",
    val id: Int = 0,
    val genres: List<Genre> = emptyList(),
    val selectedGenreId: Int? = null
)

