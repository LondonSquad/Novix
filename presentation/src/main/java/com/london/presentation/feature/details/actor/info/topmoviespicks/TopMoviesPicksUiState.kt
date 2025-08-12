package com.london.presentation.feature.details.actor.info.topmoviespicks

import com.london.domain.entity.actordetails.cast.CastDetails
import com.london.presentation.shared.base.ErrorState

data class TopMoviesPicksUiState(
    val isSaved: Boolean = false,
    val isLoading: Boolean = false,
    val errorState: ErrorState? = null,
    val actorMovieDetails: CastDetails = CastDetails(),
)
