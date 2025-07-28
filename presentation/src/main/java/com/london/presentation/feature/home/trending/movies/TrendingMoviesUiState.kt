package com.london.presentation.feature.home.trending.movies

import androidx.paging.PagingData
import com.london.domain.entity.Trending
import com.london.presentation.feature.base.ErrorState
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
    val selectedGenreId: Int? = -1
)
