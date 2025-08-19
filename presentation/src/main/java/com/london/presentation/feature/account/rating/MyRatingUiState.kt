package com.london.presentation.feature.account.rating

import com.london.domain.entity.shared.RatedMedia
import com.london.presentation.shared.base.ErrorState

data class MyRatingUiState(
    val isLoading: Boolean = false,
    val errorState: ErrorState? = null,
    val allRatedMedia: List<RatedMedia> = emptyList(),
    val ratedMovies: List<RatedMedia> = emptyList(),
    val ratedTvShows: List<RatedMedia> = emptyList(),
    val isSnackBarVisible: Boolean = false,
    val selectedRatingCategory: RatingCategory? = null,
)
