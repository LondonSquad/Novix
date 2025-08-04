package com.london.presentation.feature.list.savedlist

import androidx.compose.ui.text.input.TextFieldValue
import androidx.paging.PagingData
import com.london.domain.entity.recent.MediaType
import com.london.presentation.feature.base.ErrorState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class ListUiState(
    val isLoading: Boolean = false,
    val isGuest: Boolean = false,
    val error: ErrorState? = null,
    val items: Flow<PagingData<ListItemUi>> = flow {},
    val isSheetVisible: Boolean = false,
    val errorMessage: String? = null,
    val addListSheetState: AddSheetState = AddSheetState(),
)

data class AddSheetState(
    val listName: TextFieldValue = TextFieldValue(""),
    val originalListName: String = "",
    val listId: Int? = null,
    val mediaType: MediaType = MediaType.Movie,
    val isSheetLoading: Boolean = false,
    val errorMessage: String? = null,
)
