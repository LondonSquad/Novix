package com.london.presentation.feature.list.viewlistitems

import androidx.paging.PagingData
import com.london.domain.entity.Movie
import com.london.presentation.feature.base.ErrorState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class ViewListItemsUiState(
    val listTitle: String = "",
    val error: ErrorState? = null,
    val isLoading: Boolean = false,
    val isDeleteBottomSheetVisible: Boolean = false,
    val listItems: Flow<PagingData<Movie>> = flow {},
)