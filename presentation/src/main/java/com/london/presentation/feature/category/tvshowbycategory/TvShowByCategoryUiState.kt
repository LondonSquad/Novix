package com.london.presentation.feature.category.tvshowbycategory

import androidx.paging.PagingData
import com.london.domain.entity.TvShow
import com.london.presentation.shared.base.ErrorState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class TvShowByCategoryUiState(
    val categoryId: Int = 0,
    val error: ErrorState? = null,
    val isLoading: Boolean = false,
    val tvShowFlow: Flow<PagingData<TvShow>> = flow {}
)
