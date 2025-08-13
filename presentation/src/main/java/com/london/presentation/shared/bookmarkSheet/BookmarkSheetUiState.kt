package com.london.presentation.shared.bookmarkSheet

import com.london.presentation.shared.base.ErrorState

data class BookmarkSheetUiState(
    val lists: List<BookmarkUiList> = emptyList(),
    val selectedLists: List<UInt> = emptyList(),
    val error: ErrorState? = null,
    val isLoading: Boolean = false,
    val isGuestSession: Boolean = false,
    val isErrorSnackbarVisible: Boolean = false,
    val isSuccessSnackbarVisible: Boolean = false,
    val shouldDismiss: Boolean = false
)

data class BookmarkUiList(
    val id: UInt,
    val name: String,
    val itemCount: UShort
)
