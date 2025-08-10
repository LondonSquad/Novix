package com.london.presentation.shared.bookmarkSheet

import androidx.paging.PagingData
import com.london.presentation.shared.base.ErrorState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class BookmarkSheetUiState(
    val lists: Flow<PagingData<BookmarkUiList>> = flow {},
    val selectedLists: List<UInt> = emptyList(),
    val isSuccessSnackbarVisible: Boolean = false,
    val isErrorSnackbarVisible: Boolean = false,
    val isLoading: Boolean = false,
    val error: ErrorState? = null
)

data class BookmarkUiList(
    val id: UInt,
    val name: String,
    val itemCount: UShort
)
