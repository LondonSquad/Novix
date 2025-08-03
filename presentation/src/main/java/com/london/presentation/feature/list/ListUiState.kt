package com.london.presentation.feature.list

import androidx.compose.ui.text.input.TextFieldValue

data class ListUiState(
    val listName: TextFieldValue = TextFieldValue(""),
    val isSheetVisible: Boolean = false,
)
