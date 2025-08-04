package com.london.presentation.feature.list.savedlist

import androidx.compose.ui.text.input.TextFieldValue
import com.london.domain.entity.recent.MediaType

interface ListContract {
    fun onRetry()
    fun onLoginClick()
    fun onListClick(id: Int)
    fun onFabClick()


    fun onEditListSheetDismiss()
    fun onSaveEdit(listName: TextFieldValue)
    fun onListNameChanged(listName: TextFieldValue)
    fun onMediaTypeChanged(mediaType: MediaType)
    fun showAddListSheet(mediaType: MediaType = MediaType.Movie)
    fun showEditListSheet(listId: String, currentName: String, mediaType: MediaType)
}
