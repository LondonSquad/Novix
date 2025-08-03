package com.london.presentation.feature.list

import androidx.compose.ui.text.input.TextFieldValue
import com.london.presentation.feature.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class ListViewModel @Inject constructor(
) : BaseViewModel<ListUiState, ListEffect>(ListUiState()), EditListContract {
    override fun onEditListSheetDismiss() {
        TODO("Not yet implemented")
    }

    override fun onSaveEdit(listName: TextFieldValue) {
        TODO("Not yet implemented")
    }

    override fun onListNameChanged(listName: TextFieldValue) {
        TODO("Not yet implemented")
    }

}