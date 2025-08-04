package com.london.presentation.feature.bookmark.sheet

interface BookmarkSheetContract {
    fun onListSelected(listId: UInt)
    fun onCreateNewList()
    fun onAddToLists(selectedListsIds: List<UInt>)
    fun onDismiss()
}