package com.london.presentation.feature.list.savedlist

import androidx.paging.PagingData
import com.london.presentation.feature.base.ErrorState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class ListUiState(
    val isLoading: Boolean = false,
    val isGuest: Boolean = false,
    val error: ErrorState? = null,
    val items: Flow<PagingData<ListItemUi>> = flow {},
)