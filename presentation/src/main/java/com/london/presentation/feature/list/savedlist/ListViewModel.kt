package com.london.presentation.feature.list.savedlist

import androidx.compose.ui.text.input.TextFieldValue
import androidx.paging.PagingData
import com.london.domain.entity.recent.MediaType
import com.london.presentation.feature.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@HiltViewModel
class ListViewModel @Inject constructor() :
    BaseViewModel<ListUiState, ListEffect>(ListUiState()), ListContract {

    init {
        getSavedLists()
    }

    override fun onRetry() {
        // TODO("Not yet implemented")
    }

    override fun onLoginClick() {
        // TODO("Not yet implemented")
    }

    override fun onListClick(id: Int) {
        emitEffect(ListEffect.NavigateToDetails(id))
    }

    override fun onFabClick() {
        /*TODO*/
    }

    override fun onEditListSheetDismiss() {
        /*TODO*/
    }

    override fun onSaveEdit(listName: TextFieldValue) {
        /*TODO*/
    }

    override fun onListNameChanged(listName: TextFieldValue) {
        /*TODO*/
    }

    override fun onMediaTypeChanged(mediaType: MediaType) {
        /*TODO*/
    }

    override fun showAddListSheet(mediaType: MediaType) {
        /*TODO*/
    }

    override fun showEditListSheet(listId: String, currentName: String, mediaType: MediaType) {
        /*TODO*/
    }

    private fun dummyItems(): Flow<PagingData<ListItemUi>> {
        val list =  //emptyList<SavedListItemUi>()
            List(5) { index ->
                ListItemUi(
                    id = index,
                    title = "Dummy List #$index",
                    count = (1..10).random()
                )
            }
        return flowOf(PagingData.from(list))
    }

    private fun getSavedLists() {
        updateState {
            copy(isLoading = false, items = dummyItems())
        }
    }
}
