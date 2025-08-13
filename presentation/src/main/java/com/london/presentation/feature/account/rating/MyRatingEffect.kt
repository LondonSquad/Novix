package com.london.presentation.feature.account.rating

sealed interface MyRatingEffect {
    data object BackNavigation : MyRatingEffect
    data class ToMovieNavigation(val id: Int) : MyRatingEffect
    data class ToTvShowNavigation(val id: Int) : MyRatingEffect
}
