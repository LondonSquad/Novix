package com.london.presentation.feature.list.viewlistitems

import com.london.domain.entity.recent.MediaType
import com.london.presentation.feature.base.BaseViewModel
import com.london.presentation.feature.list.viewlistitems.uistate.ItemsType
import com.london.presentation.feature.list.viewlistitems.uistate.ViewListItemsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ViewListItemsViewModel @Inject constructor() :
    BaseViewModel<ViewListItemsUiState, ViewListItemsEffect>(ViewListItemsUiState()),
    ViewListItemsContract {

    override fun onBack() {
        emitEffect(ViewListItemsEffect.NavigateBack)
    }

    override fun onRetry() {
        //TODO("handle retry to fetch data")
    }

    override fun onEditClick() {
        //TODO("handle on edit click bottom sheet")
    }

    override fun onDeleteClick() {
        //TODO("handle on delete click bottom sheet")
    }

    override fun onMovieClick(id: Int) {
        emitEffect(ViewListItemsEffect.NavigationMovieDetails(id))
    }

    override fun onTvShowClick(id: Int) {
        emitEffect(ViewListItemsEffect.NavigationTvShowDetails(id))
    }

    override fun onItemsTypeClick(itemsType: ItemsType) {
        updateState { copy(selectedItemsType = itemsType) }
    }

    override fun onRemoveMediaClick(id: Int, type: MediaType) {
        //TODO("handle remove media from list")
    }
}
