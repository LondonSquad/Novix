package com.london.presentation.feature.accountinfo.rating

sealed interface MyRatingEffect {
    data object NavigateBack : MyRatingEffect
    data class NavigateToMovie(val movieId: Int) : MyRatingEffect
    data class NavigateToTvShow(val tvShowId: Int) : MyRatingEffect
}
