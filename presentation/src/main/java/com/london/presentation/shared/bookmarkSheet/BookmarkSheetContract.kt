package com.london.presentation.shared.bookmarkSheet

interface BookmarkSheetContract {
    fun onListSelected(listId: UInt)
    fun onCreateNewList()
    fun onAddToLists(bookmarkedId: UInt)
    fun onDismiss()

    fun onSnackbarShown()

    fun onLoginClick()
}