package com.london.presentation.feature.list.viewlistitems

import com.london.domain.entity.recent.MediaType
import com.london.presentation.feature.list.viewlistitems.uistate.ItemsType

interface ViewListItemsContract {
    fun onBack()
    fun onRetry()
    fun onEditClick()
    fun onDeleteClick()
    fun onRemoveMediaClick(id: Int, type: MediaType)
    fun onMovieClick(id: Int)
    fun onTvShowClick(id: Int)
    fun onItemsTypeClick(itemsType: ItemsType)
}