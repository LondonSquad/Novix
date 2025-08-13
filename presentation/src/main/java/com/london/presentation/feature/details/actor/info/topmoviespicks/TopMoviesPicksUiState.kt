package com.london.presentation.feature.details.actor.info.topmoviespicks

import com.london.domain.entity.actordetails.cast.ActorMediaDetails
import com.london.presentation.shared.base.ErrorState

data class TopMoviesPicksUiState(
    val isLoading: Boolean = false,
    val errorState: ErrorState? = null,
    val mediaDetails: ActorMediaDetails = ActorMediaDetails(),
)
