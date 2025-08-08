package com.london.presentation.feature.myrating

import com.london.domain.entity.myrating.AllRatedContent
import com.london.domain.entity.myrating.RatedMovie
import com.london.domain.entity.myrating.RatedTvShow
import com.london.presentation.shared.base.ErrorState

data class MyRatingUiState(
    val movies: List<RatedMovie> = emptyList(),
    val tvShows: List<RatedTvShow> = emptyList(),
    val allRated: AllRatedContent = AllRatedContent(emptyList(), emptyList()),
    val errorState: ErrorState? = null,
    val isLoading: Boolean = false,
    val rate: Int = 0,
    val isDeleteClicked: Boolean = false,
    val backdropPath: String = "",
    val id: Int = 0,
    val selectedRatingCategory: RatingCategory? = null,
)
