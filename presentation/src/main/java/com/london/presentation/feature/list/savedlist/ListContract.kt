package com.london.presentation.feature.list.savedlist

interface ListContract {
    fun onRetry()
    fun onLoginClick()
    fun onListClick(id: Int)
    fun onFabClick()
}
