package com.london.presentation.feature.list.savedlist

interface SavedListContract {
    fun onRetry()
    fun onLoginClick()
    fun onListClick(id: Int)
    fun onFabClick()
}
