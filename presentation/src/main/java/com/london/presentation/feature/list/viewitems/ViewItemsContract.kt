package com.london.presentation.feature.list.viewitems

interface ViewListItemsContract {
    fun onBackClick()
    fun onRetryClick()
    fun onDeleteClick()
    fun onConfirmDelete()
    fun onMovieClick(id: Int)
    fun onRemoveMovieClick(id: Int)
    fun onDeleteBottomSheetDismiss()
    fun resetSnackBarErrorState()
    fun resetMovieSnackBarSuccessState()
    fun resetListSnackBarSuccessState()
}
