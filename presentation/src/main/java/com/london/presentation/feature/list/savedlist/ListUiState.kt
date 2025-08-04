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
    val listName: TextFieldValue = TextFieldValue(""),
    val originalListName: String = "",
    val isSheetVisible: Boolean = false,
    val errorMessage: String? = null,
    val listId: String? = null,
    val sheetMode: ListSheetMode = ListSheetMode.ADD,
    val mediaType: MediaType = MediaType.Movie,
    val isSheetLoading: Boolean = false,


)

enum class ListSheetMode {
    ADD,
    EDIT
}