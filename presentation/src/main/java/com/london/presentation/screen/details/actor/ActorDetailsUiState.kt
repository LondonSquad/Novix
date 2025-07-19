package com.london.presentation.screen.details.actor

import com.london.domain.entity.actordetails.ActorDetails
import com.london.domain.entity.actordetails.actorimage.ImageDetails
import com.london.domain.entity.actordetails.actormovie.ActorMovieDetails
import com.london.domain.entity.actordetails.actortvshow.ActorTvShowDetails

data class ActorDetailsUiState(
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
)