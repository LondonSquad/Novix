package com.london.presentation.feature.list.savedlist

sealed interface SavedListEffect {
    data class NavigateToDetails(val id: Int) : SavedListEffect
    object AddNewList : SavedListEffect
}
