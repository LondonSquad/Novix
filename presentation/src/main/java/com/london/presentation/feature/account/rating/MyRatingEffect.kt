package com.london.presentation.feature.account.rating

sealed interface MyRatingEffect {
    data object NavigationBack : MyRatingEffect
    data class NavigationMovieDetails(val id: Int) : MyRatingEffect
    data class NavigationTvShowDetails(val id: Int) : MyRatingEffect
}
