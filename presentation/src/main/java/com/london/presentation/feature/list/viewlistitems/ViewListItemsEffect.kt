package com.london.presentation.feature.list.viewlistitems

interface ViewListItemsEffect {
    data class NavigationTvShowDetails(val id: Int) : ViewListItemsEffect
    data class NavigationMovieDetails(val id: Int) : ViewListItemsEffect
    object NavigateBack : ViewListItemsEffect
}
