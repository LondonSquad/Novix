package com.london.presentation.shared.bookmarkSheet

interface BookmarkSheetContract {
    fun onSheetShown(movieId: Int)
    fun onListSelected(listId: Int)
    fun onCreateNewList()
    fun onAddToLists(bookmarkedId: Int)
    fun onDismiss()
    fun onSnackbarShown()
    fun onLoginClick()
}