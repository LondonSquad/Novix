package com.london.presentation.feature.accountinfo.rating

import com.london.domain.entity.RatedMedia
import com.london.presentation.shared.base.ErrorState

data class MyRatingUiState(
    val allRatedMedia: List<RatedMedia> = emptyList(),
    val ratedMovies: List<RatedMedia> = emptyList(),
    val ratedTvShows: List<RatedMedia> = emptyList(),
    val errorState: ErrorState? = null,
    val isLoading: Boolean = false,
    val rate: Int = 0,
    val isDeleteClicked: Boolean = false,
    val backdropPath: String = "",
    val id: Int = 0,
    val selectedRatingCategory: RatingCategory? = null,
)
