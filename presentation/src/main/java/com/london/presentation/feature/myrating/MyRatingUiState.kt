package com.london.presentation.feature.myrating

import com.london.domain.entity.myrating.RatedMovie
import com.london.domain.entity.myrating.RatedTvShow
import com.london.presentation.shared.base.ErrorState

data class MyRatingUiState(
    val movies: List<RatedMovie> = emptyList(),
    val tvShows: List<RatedTvShow> = emptyList(),
    val allRated: Pair<List<RatedTvShow>,List<RatedMovie>> = Pair(emptyList(), emptyList()),
    val errorState: ErrorState? = null,
    val isLoading: Boolean = false,
    val rate: Int = 0,
    val isDeleteClicked: Boolean = false,
    val backdropPath: String = "",
    val id: Int = 0,
)
