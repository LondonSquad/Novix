package com.london.presentation.feature.list.details

import androidx.paging.PagingData
import com.london.domain.entity.movie.Movie
import com.london.presentation.shared.base.ErrorState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class MovieListDetailsUiState(
    val listTitle: String = "",
    val error: ErrorState? = null,
    val isLoading: Boolean = false,
    val isSnackBarErrorVisible: Boolean = false,
    val isMovieSnackBarSuccessVisible: Boolean = false,
    val isListSnackBarSuccess: Boolean = false,
    val isDeleteBottomSheetVisible: Boolean = false,
    val listItems: Flow<PagingData<Movie>> = flow {},
)
