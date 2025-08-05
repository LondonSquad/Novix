package com.london.presentation.feature.list.viewlistitems.uistate

import androidx.paging.PagingData
import com.london.presentation.feature.base.ErrorState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class ViewListItemsUiState(
    val listTitle: String = "",
    val error: ErrorState? = null,
    val isLoading: Boolean = false,
    val isEditBottomSheetVisible: Boolean = false,
    val isDeleteBottomSheetVisible: Boolean = false,
    val selectedItemsType: ItemsType = ItemsType.All,
    val listItems: Flow<PagingData<MediaUi>> = flow {},
)