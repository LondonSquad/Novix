package com.london.presentation.feature.list.savedlist

import com.london.domain.entity.recent.MediaType

sealed interface ListEffect {
    data class NavigateToDetails(val id: Int) : ListEffect
    object ShowAddListSheet : ListEffect
    data class ShowEditListSheet(val listId: String, val name: String, val mediaType: MediaType) :
        ListEffect

}
