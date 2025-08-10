package com.london.presentation.feature.list.viewitems

interface ViewListItemsContract {
    fun onBack()
    fun onRetry()
    fun onDeleteClick()
    fun onConfirmDelete()
    fun onMovieClick(id: Int)
    fun onRemoveMovieClick(id: Int)
    fun onDeleteBottomSheetDismiss()
}