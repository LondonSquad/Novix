package com.london.presentation.feature.list.savedlist

sealed interface ListEffect {
    data class NavigateToDetails(val id: Int) : ListEffect
}
