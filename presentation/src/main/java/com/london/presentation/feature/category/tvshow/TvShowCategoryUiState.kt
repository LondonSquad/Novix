package com.london.presentation.feature.category.tvshow

import androidx.paging.PagingData
import com.london.domain.entity.TvShow
import com.london.presentation.shared.base.ErrorState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class TvShowCategoryUiState(
    val categoryId: Int = 0, //toDo() category id will replace with enum
    val error: ErrorState? = null,
    val isLoading: Boolean = false,
    val tvShowFlow: Flow<PagingData<TvShow>> = flow {}
)
