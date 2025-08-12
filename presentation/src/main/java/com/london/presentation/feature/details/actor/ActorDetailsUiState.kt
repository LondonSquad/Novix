package com.london.presentation.feature.details.actor

import com.london.domain.entity.actordetails.ActorDetails
import com.london.domain.entity.actordetails.cast.CastDetails
import com.london.presentation.shared.base.ErrorState

data class ActorDetailsUiState(
    val movieId: Int = 1,
    val tvShowId: Int = 1,
    val error: ErrorState? = null,
    val isLoading: Boolean = false,
    val movieError: Boolean = false,
    val tvShowError: Boolean = false,
    val actorTvShowDetails: CastDetails? = null,
    val actorMovieDetails: CastDetails? = null,
    val actorImageDetails: List<String>? = null,
    val actorDetails: ActorDetails = ActorDetails(),
)
