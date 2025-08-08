package com.london.presentation.feature.myrating

import com.london.domain.entity.myrating.AllRatedContent
import com.london.presentation.shared.base.ErrorState

data class MyRatingUiState(
    val allRated: AllRatedContent = AllRatedContent(emptyList()),
    val errorState: ErrorState? = null,
    val isLoading: Boolean = false,
    val rate: Int = 0,
    val isDeleteClicked: Boolean = false,
    val backdropPath: String = "",
    val id: Int = 0,
    val selectedRatingCategory: RatingCategory? = null,
)
