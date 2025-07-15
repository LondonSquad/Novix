package com.london.presentation.screen.details.actordetails.toptvshowspicks

import com.london.domain.entity.Movie

data class TopTvShowsPicksUiState(
    val movies: List<Movie> = listOf(),
)