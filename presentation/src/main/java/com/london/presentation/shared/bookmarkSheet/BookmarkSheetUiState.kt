package com.london.presentation.shared.bookmarkSheet

import com.london.presentation.shared.base.ErrorState

data class BookmarkSheetUiState(
    val lists: List<BookmarkUiList> = emptyList(),
    val selectedLists: List<Int> = emptyList(),
    val error: ErrorState? = null,
    val isLoading: Boolean = false,
    val isAddingToList: Boolean = false,
    val isGuestSession: Boolean = false,
    val isErrorSnackbarVisible: Boolean = false,
    val isSuccessSnackbarVisible: Boolean = false,
    val shouldDismiss: Boolean = false
)

data class BookmarkUiList(
    val id: Int,
    val name: String,
    val itemCount: Int
)
