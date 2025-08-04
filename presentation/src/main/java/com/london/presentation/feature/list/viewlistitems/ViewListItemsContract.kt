package com.london.presentation.feature.list.viewlistitems

import com.london.domain.entity.recent.MediaType
import com.london.presentation.feature.list.viewlistitems.uistate.ItemsType

interface ViewListItemsContract {
    fun onBack()
    fun onRetry()
    fun onEditClick()
    fun onDeleteClick()
    fun onMovieClick(id: Int)
    fun onTvShowClick(id: Int)
    fun onItemsTypeClick(itemsType: ItemsType)
    fun onRemoveMediaClick(id: Int, type: MediaType)
}

fun defaultContractViewListItems() = object : ViewListItemsContract {
    override fun onBack() {}
    override fun onRetry() {}
    override fun onEditClick() {}
    override fun onDeleteClick() {}
    override fun onMovieClick(id: Int) {}
    override fun onTvShowClick(id: Int) {}
    override fun onItemsTypeClick(itemsType: ItemsType) {}
    override fun onRemoveMediaClick(id: Int, type: MediaType) {}
}