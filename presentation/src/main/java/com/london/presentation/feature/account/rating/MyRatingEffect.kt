package com.london.presentation.feature.account.rating

sealed interface MyRatingEffect {
    data object BackNavigation : MyRatingEffect
    data class MovieNavigation(val id: Int) : MyRatingEffect
    data class TvShowNavigation(val id: Int) : MyRatingEffect
}
