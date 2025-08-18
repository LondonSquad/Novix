package com.london.presentation.feature.category.movie

import androidx.paging.PagingData
import com.london.domain.entity.movie.Movie
import com.london.presentation.shared.base.ErrorState
import com.london.presentation.shared.genre.MovieGenreUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class MovieCategoryUiState(
    val error: ErrorState? = null,
    val isLoading: Boolean = false,
    val genre: MovieGenreUi = MovieGenreUi.All,
    val moviesFlow: Flow<PagingData<Movie>> = flow {}
)
