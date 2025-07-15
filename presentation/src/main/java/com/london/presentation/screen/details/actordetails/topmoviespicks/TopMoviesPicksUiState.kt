package com.london.presentation.screen.details.actordetails.topmoviespicks

import com.london.domain.entity.Movie

data class TopMoviesPicksUiState(
    val movies: List<Movie> = listOf(),
)