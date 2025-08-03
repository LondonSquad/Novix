package com.london.presentation.feature.list.savedlist

interface SavedListContract {
    fun onRetry()
    fun onLoginClick()
    fun onItemCountClick(id: Int)
    fun onFabClick()
}
