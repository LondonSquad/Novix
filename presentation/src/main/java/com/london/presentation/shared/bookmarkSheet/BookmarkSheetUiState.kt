package com.london.presentation.shared.bookmarkSheet

import com.london.domain.entity.Movie
import com.london.presentation.shared.base.ErrorState


data class BookmarkSheetUiState(
    val listedMovies: Set<Movie> = emptySet(),
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
