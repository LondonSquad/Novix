package com.london.presentation.feature.list.savedlist

import androidx.compose.ui.text.input.TextFieldValue
import com.london.domain.entity.recent.MediaType

interface ListContract {

    fun onRetry()
    fun onFabClick()
    fun onLoginClick()
    fun onListClick(id: Int)
    fun onAddList(listName: String)
    fun setAddListSheetVisible(visible: Boolean)
    fun onListNameChanged(listName: TextFieldValue)
}


fun defaultContractList() = object : ListContract {

    override fun onRetry() {}
    override fun onFabClick() {}
    override fun onLoginClick() {}
    override fun onListClick(id: Int) {}
    override fun onAddList(listName: String) {}
    override fun setAddListSheetVisible(visible: Boolean) {}
    override fun onListNameChanged(listName: TextFieldValue) {}
}