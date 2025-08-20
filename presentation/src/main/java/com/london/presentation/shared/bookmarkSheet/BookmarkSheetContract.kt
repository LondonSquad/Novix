package com.london.presentation.shared.bookmarkSheet

interface BookmarkSheetContract {
    fun onDismiss()
    fun onLoginClick()
    fun onSnackBarShown()
    fun onCreateNewList()
    fun onSheetShown(movieId: Int)
    fun onListSelected(listId: Int)
    fun onAddToLists(bookmarkedId: Int)
}
