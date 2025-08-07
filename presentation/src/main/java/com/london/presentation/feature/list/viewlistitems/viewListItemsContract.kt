package com.london.presentation.feature.list.viewlistitems

interface ViewListItemsContract {
    fun onBack()
    fun onRetry()
    fun onDeleteClick()
    fun onMovieClick(id: Int)
    fun onRemoveMovieClick(id: Int)
}