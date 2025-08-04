package com.london.presentation.feature.list.savedlist

import androidx.paging.PagingData
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
       // TODO("Not yet implemented")
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
