package com.london.presentation.shared.bookmarkSheet

import com.london.presentation.shared.base.ErrorState

data class BookmarkSheetUiState(
    val error: ErrorState? = null,
    val isLoading: Boolean = false,
    val shouldDismiss: Boolean = false,
    val isAddingToList: Boolean = false,
    val isGuestSession: Boolean = false,
    val selectedLists: List<Int> = emptyList(),
    val isErrorSnackBarVisible: Boolean = false,
    val lists: List<BookmarkUiList> = emptyList(),
    val isSuccessSnackBarVisible: Boolean = false,
)

data class BookmarkUiList(
    val id: Int,
    val name: String,
    val itemCount: Int
)
