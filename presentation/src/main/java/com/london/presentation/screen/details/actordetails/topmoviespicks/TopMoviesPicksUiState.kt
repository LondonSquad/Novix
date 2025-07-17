package com.london.presentation.screen.details.actordetails.topmoviespicks

import com.london.domain.entity.actordetails.actormovie.ActorMovieDetails

data class TopMoviesPicksUiState(
    val movieDetails: ActorMovieDetails = ActorMovieDetails(),
    val isSaved: Boolean = false,
    val backdropPath: String = "",
    val id: Int = 0,
    val numberOfMovies: Int = 0,
)
