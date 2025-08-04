package com.london.presentation.feature.bookmark.sheet

import com.london.presentation.feature.base.ErrorState

data class BookmarkSheetUiState(
    val lists: List<BookmarkUiList> = emptyList(),
    val selectedLists: List<UInt> = emptyList(),
    val isLoading: Boolean = false,
    val error: ErrorState? = null
)

data class BookmarkUiList(
    val id: UInt,
    val name: String,
    val itemCount: UShort
)
