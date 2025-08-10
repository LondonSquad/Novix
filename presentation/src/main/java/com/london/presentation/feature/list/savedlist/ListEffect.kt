package com.london.presentation.feature.list.savedlist

sealed interface ListEffect {
    data object NavigateToLogin : ListEffect
    data class NavigateToDetails(val id: Int) : ListEffect
}
