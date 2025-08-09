package com.london.presentation.feature.category.movie

import androidx.paging.PagingData
import com.london.domain.entity.Movie
import com.london.presentation.shared.base.ErrorState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class MovieCategoryUiState(
    val categoryId: Int = 0,
    val error: ErrorState? = null,
    val isLoading: Boolean = false,
    val movies: Flow<PagingData<Movie>> = flow {}
)
