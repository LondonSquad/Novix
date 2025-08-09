package com.london.presentation.feature.search.details.actor

import com.london.domain.entity.actordetails.ActorDetails
import com.london.domain.entity.actordetails.actorimage.ImageDetails
import com.london.domain.entity.actordetails.actormovie.ActorMovieDetails
import com.london.domain.entity.actordetails.actortvshow.ActorTvShowDetails
import com.london.presentation.shared.base.ErrorState

data class ActorDetailsUiState(
    val actorId: Int = 1,
    val movieId: Int = 1,
    val tvShowId: Int = 1,
    val actorName: String = "",
    val isEmpty: Boolean = false,
    val error: ErrorState? = null,
    val expanded: Boolean = false,
    val isLoading: Boolean = false,
    val actorBirthday: String = "",
    val movieError: Boolean = false,
    val actorBiography: String = "",
    val tvShowError: Boolean = false,
    val actorDeathDay: String? = null,
    val actorPlaceOfBirth: String = "",
    val knownForDepartment: String = "",
    val actorDetails: ActorDetails = ActorDetails(),
    val actorImageDetails: List<ImageDetails>? = null,
    val actorMovieDetails: ActorMovieDetails? = null,
    val actorTvShowDetails: ActorTvShowDetails? = null,
)