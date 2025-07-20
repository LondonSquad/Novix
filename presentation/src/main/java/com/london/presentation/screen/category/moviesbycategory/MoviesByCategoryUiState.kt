package com.london.presentation.screen.category.moviesbycategory

import androidx.paging.PagingData
import com.london.domain.entity.Movie
import com.london.presentation.screen.base.ErrorState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class MoviesByCategoryUiState(
    val isLoading: Boolean = false,
    val movies: Flow<PagingData<Movie>> = flow {},
    val categoryId: Int = 28,
    val error: ErrorState? = null
)
