package com.london.presentation.feature.details.actor.info.topmoviespicks

import com.london.domain.entity.actordetails.cast.CastDetails
import com.london.presentation.shared.base.ErrorState

data class TopMoviesPicksUiState(
    val id: Int = 0,
    val isSaved: Boolean = false,
    val backdropPath: String = "",
    val isLoading: Boolean = false,
    val errorState: ErrorState? = null,
    val actorMovieDetails: CastDetails = CastDetails(),
)
