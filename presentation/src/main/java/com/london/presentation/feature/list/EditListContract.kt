package com.london.presentation.feature.list

import androidx.compose.ui.text.input.TextFieldValue

interface EditListContract {
    fun onEditListSheetDismiss()
    fun onSaveEdit(listName: TextFieldValue)
    fun onListNameChanged(listName: TextFieldValue)
}