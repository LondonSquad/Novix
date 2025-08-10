package com.london.presentation.feature.list.viewitems

interface ViewItemsEffect {
    object NavigateBack : ViewItemsEffect
    data class NavigationMovieDetails(val id: Int) : ViewItemsEffect
}
