package com.london.presentation.feature.myrating

sealed interface MyRatingEffect {
    data class NavigateToMovie(val movieId: Int) : MyRatingEffect
    data class NavigateToTvShow(val tvShowId: Int) : MyRatingEffect
    data object NavigateBack : MyRatingEffect
}
