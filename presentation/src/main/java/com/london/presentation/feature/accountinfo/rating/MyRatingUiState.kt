package com.london.presentation.feature.accountinfo.rating

import com.london.domain.entity.RatedMedia
import com.london.presentation.shared.base.ErrorState

data class MyRatingUiState(
    val id: Int = 0,
    val rate: Int = 0,
    val backdropPath: String = "",
    val isLoading: Boolean = false,
    val errorState: ErrorState? = null,
    val isDeleteClicked: Boolean = false,
    val allRatedMedia: List<RatedMedia> = emptyList(),
    val ratedMovies: List<RatedMedia> = emptyList(),
    val ratedTvShows: List<RatedMedia> = emptyList(),
    val errorState: ErrorState? = null,
    val isLoading: Boolean = false,
    val rate: Int = 0,
    val isSnackBarVisible: Boolean = false,
    val backdropPath: String = "",
    val id: Int = 0,
    val selectedRatingCategory: RatingCategory? = null,
    )
