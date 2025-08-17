package com.london.presentation.feature.account.rating

sealed interface MyRatingEffect {
    data object BackNavigation : MyRatingEffect
    data class MovieDetailsNavigation(val id: Int) : MyRatingEffect
    data class TvShowDetailsNavigation(val id: Int) : MyRatingEffect
}
