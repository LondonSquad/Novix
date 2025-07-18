package com.london.presentation.screen.category.moviesbycategory

import androidx.paging.PagingData
import com.london.domain.entity.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class MoviesByCategoryUiState(
    val movies: Flow<PagingData<Movie>> = flow {},
    val categoryId: Int = 28,
    val error: String? = null
)
