package com.london.presentation.feature.list.savedlist

import androidx.compose.ui.text.input.TextFieldValue
import com.london.domain.entity.recent.MediaType

interface ListContract {

    fun onRetry()
    fun onFabClick()
    fun onLoginClick()
    fun showBottomSheet()
    fun onListClick(id: Int)
    fun onAddListSheetDismiss()
    fun onAddList(listName: String)
    fun onListNameChanged(listName: TextFieldValue)
}


fun defaultContractList() = object : ListContract {

    override fun onRetry() {}
    override fun onFabClick() {}
    override fun onLoginClick() {}
    override fun showBottomSheet() {}
    override fun onListClick(id: Int) {}
    override fun onAddListSheetDismiss() {}
    override fun onAddList(listName: String) {}
    override fun onListNameChanged(listName: TextFieldValue) {}
}