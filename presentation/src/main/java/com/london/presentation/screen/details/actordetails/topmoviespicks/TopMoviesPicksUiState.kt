package com.london.presentation.screen.details.actordetails.topmoviespicks

import com.london.domain.entity.actordetails.actormovie.ActorMovieDetails
import com.london.presentation.screen.base.ErrorState

data class TopMoviesPicksUiState(
    val movieDetails: ActorMovieDetails = ActorMovieDetails(),
    val errorState: ErrorState? = null,
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val backdropPath: String = "",
    val id: Int = 0,
)
