package com.london.presentation.feature.list.viewlistitems

interface ViewListItemsEffect {
    object NavigateBack : ViewListItemsEffect
    data class NavigationMovieDetails(val id: Int) : ViewListItemsEffect
    data class NavigationTvShowDetails(val id: Int) : ViewListItemsEffect
}
