package com.london.presentation.feature.myrating

import androidx.paging.PagingData
import com.london.presentation.shared.base.ErrorState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class MyRatingUiState(
    val movieFlow: Flow<PagingData<RatedMovie>> = flow {},
    val tvShowFlow: Flow<PagingData<RatedTvShow>> = flow {},
    val errorState: ErrorState? = null,
    val isLoading: Boolean = false,
    val isDeleteClicked: Boolean = false,
    val backdropPath: String = "",
    val id: Int = 0,
)
