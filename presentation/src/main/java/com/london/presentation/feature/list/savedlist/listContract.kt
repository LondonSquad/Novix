package com.london.presentation.feature.list.savedlist

import androidx.compose.ui.text.input.TextFieldValue
import com.london.domain.entity.recent.MediaType

interface ListContract {
    fun onRetry()
    fun onLoginClick()
    fun onListClick(id: Int)
    fun onFabClick()

    fun onAddList(listName: TextFieldValue)
    fun onAddListSheetDismiss()
    fun onListNameChanged(listName: TextFieldValue)
    fun onMediaTypeChanged(mediaType: MediaType)
    fun showAddListSheet(mediaType: MediaType = MediaType.Movie)
}


fun defaultContractList() = object : ListContract {
    override fun onRetry() {}
    override fun onLoginClick() {}
    override fun onListClick(id: Int) {}
    override fun onFabClick() {}
    override fun onAddList(listName: TextFieldValue) {}
    override fun onAddListSheetDismiss() {}
    override fun onListNameChanged(listName: TextFieldValue) {}
    override fun onMediaTypeChanged(mediaType: MediaType) {}
    override fun showAddListSheet(mediaType: MediaType) {}
}