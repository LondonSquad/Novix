package com.london.presentation.feature.bookmark.sheet

import com.london.presentation.feature.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookmarkSheetViewModel @Inject constructor(
) : BaseViewModel<BookmarkSheetUiState, BookmarkSheetEffect>(BookmarkSheetUiState()),
    BookmarkSheetContract
{
    // TODO: Handle fetching lists

    override fun onListSelected(listId: UInt) = updateState {
        copy(selectedLists = selectedLists.toMutableList().apply { add(listId) })
    }

    override fun onCreateNewList() {
        TODO("Not yet implemented, Waiting on domain & data implementation")
    }

    override fun onAddToLists(selectedListsIds: List<UInt>) {
        TODO("Not yet implemented, Waiting on domain & data implementation")
    }

    override fun onDismiss() = updateState { copy(selectedLists = emptyList()) }

}