package com.london.presentation.feature.home.trending.movie

import androidx.paging.PagingData
import com.london.domain.entity.Trending
import com.london.presentation.shared.base.ErrorState
import com.london.presentation.utils.MovieGenre
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class TrendingMoviesUiState(
    val id: Int = 0,
    val isSaved: Boolean = false,
    val selectedGenreId: Int? = 0,
    val backdropPath: String = "",
    val isLoading: Boolean = false,
    val errorState: ErrorState? = null,
    val moviesFlow: Flow<PagingData<Trending>> = flow {},
    val movieGenres: List<MovieGenre> = MovieGenre.entries.toList()
)
