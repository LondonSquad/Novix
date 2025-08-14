package com.london.presentation.feature.details.actor

import com.london.domain.entity.actordetails.ActorDetails
import com.london.domain.entity.actordetails.cast.ActorMediaDetails
import com.london.presentation.shared.base.ErrorState

data class ActorDetailsUiState(
    val error: ErrorState? = null,
    val isLoading: Boolean = false,
    val movieError: Boolean = false,
    val tvShowError: Boolean = false,
    val actorImageDetails: List<String>? = null,
    val actorDetails: ActorDetails = ActorDetails(),
    val actorMovieDetails: ActorMediaDetails? = null,
    val actorTvShowDetails: ActorMediaDetails? = null,
)
