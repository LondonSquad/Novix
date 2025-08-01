package com.london.presentation.feature.details.actor

import com.london.domain.entity.actordetails.ActorDetails
import com.london.domain.entity.actordetails.actorimage.ImageDetails
import com.london.domain.entity.actordetails.actormovie.ActorMovieDetails
import com.london.domain.entity.actordetails.actortvshow.ActorTvShowDetails
import com.london.presentation.feature.base.ErrorState

data class ActorDetailsUiState(
    val isLoading: Boolean = false,
    val error: ErrorState? = null,
    val isEmpty: Boolean = false,
    val actorDetails: ActorDetails = ActorDetails(),
    val actorImageDetails: List<ImageDetails>? = null,
    val actorMovieDetails: ActorMovieDetails? = null,
    val actorTvShowDetails: ActorTvShowDetails? = null,
    val actorId: Int = 0,
    val actorName: String = "",
    val actorBirthday: String = "",
    val actorDeathDay: String? = null,
    val actorPlaceOfBirth: String = "",
    val actorBiography: String = "",
    val knownForDepartment: String = "",
    val movieError: Boolean = false,
    val tvShowError: Boolean = false,
    val tvShowId: Int = 1,
    val movieId: Int = 1,
    val expanded: Boolean = false
)