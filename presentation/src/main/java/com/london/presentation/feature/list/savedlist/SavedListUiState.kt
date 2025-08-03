package com.london.presentation.feature.list.savedlist

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class SavedListUiState(
    val isLoading: Boolean = false,
    val isGuest: Boolean = false,
    val errorMessage: String? = null,
    val items: Flow<PagingData<SavedListItemUi>> = flow {},
)