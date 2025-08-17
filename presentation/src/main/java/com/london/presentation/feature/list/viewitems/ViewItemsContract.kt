package com.london.presentation.feature.list.viewitems

interface ViewItemsContract {
    fun onBackClick()
    fun onRetryClick()
    fun onDeleteClick()
    fun onConfirmDeleteClick()
    fun onMovieClick(id: Int)
    fun onRemoveMovieClick(id: Int)
    fun onDeleteBottomSheetDismissClick()
}