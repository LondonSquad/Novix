package com.london.presentation.screen.home

import com.london.domain.entity.popular.PopularMovie
import com.london.presentation.screen.base.ErrorState

data class HomeScreenUiState(
    val isLoading: Boolean = false,
    val error: ErrorState? = null,
    val popularMovies: List<PopularMovie> = emptyList()
)
