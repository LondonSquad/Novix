package com.london.presentation.feature.list.savedlist

import androidx.compose.ui.text.input.TextFieldValue
import com.london.domain.entity.recent.MediaType

interface ListContract {
    fun onRetry()
    fun onLoginClick()
    fun onListClick(id: Int)
    fun onFabClick()


    fun onEditAddListSheetDismiss()
    fun onSaveEdit(listName: TextFieldValue)
    fun onListNameChanged(listName: TextFieldValue)
    fun onMediaTypeChanged(mediaType: MediaType)
    fun showAddListSheet(mediaType: MediaType = MediaType.Movie)
    fun showEditListSheet(listId: Int, currentName: String, mediaType: MediaType)
}


fun defaultContractList() = object : ListContract {
    override fun onRetry() {}
    override fun onLoginClick() {}
    override fun onListClick(id: Int) {}
    override fun onFabClick() {}
    override fun onEditAddListSheetDismiss() {}
    override fun onSaveEdit(listName: TextFieldValue) {}
    override fun onListNameChanged(listName: TextFieldValue) {}
    override fun onMediaTypeChanged(mediaType: MediaType) {}
    override fun showAddListSheet(mediaType: MediaType) {}
    override fun showEditListSheet(
        listId: Int,
        currentName: String,
        mediaType: MediaType
    ) {
    }
}