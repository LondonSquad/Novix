package com.london.presentation.screen.home.trending.movie

import androidx.paging.PagingData
import com.london.domain.entity.trending.Trending
import com.london.presentation.screen.base.ErrorState
import com.london.presentation.utils.MovieGenre
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class TrendingMoviesUiState(
    val moviesFlow: Flow<PagingData<Trending>> = flow {},
    val movieGenres: List<MovieGenre> = MovieGenre.entries.toList(),
    val errorState: ErrorState? = null,
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val backdropPath: String = "",
    val id: Int = 0,
    val selectedGenreId: Int? = null
)

