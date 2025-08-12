package com.london.presentation.feature.details.actor

import com.london.domain.entity.actordetails.ActorDetails
import com.london.domain.entity.actordetails.cast.CastDetails
import com.london.presentation.shared.base.ErrorState

data class ActorDetailsUiState(
    val actorId: Int = 1,
    val movieId: Int = 1,
    val tvShowId: Int = 1,
    val actorName: String = "",
    val error: ErrorState? = null,
    val isLoading: Boolean = false,
    val actorBirthday: String = "",
    val movieError: Boolean = false,
    val actorBiography: String = "",
    val tvShowError: Boolean = false,
    val actorDeathDay: String? = null,
    val actorPlaceOfBirth: String = "",
    val knownForDepartment: String = "",
    val castDetails: CastDetails? = null,
    val actorMovieDetails: CastDetails? = null,
    val actorImageDetails: List<String>? = null,
    val actorDetails: ActorDetails = ActorDetails(),
)
