package com.london.presentation.screen.details.actor

import com.london.domain.entity.actordetails.ActorDetails
import com.london.domain.entity.actordetails.actorimage.ImageDetails
import com.london.domain.entity.actordetails.actormovie.ActorMovieCastMemberEntity

data class ActorDetailsUiState(
    val actorDetails: ActorDetails = ActorDetails(
        id = 0,
        name = "",
        gender = 1,
        adult = false,
        birthday = "",
        deathDay = "",
        placeOfBirth = "",
        biography = "",
        alsoKnownAs = emptyList(),
        homePage = "",
        imdbId = "",
        knownForDepartment = "",
        popularity = 0.0,
        profilePath = ""
    ),
    val actorImageDetails: List<ImageDetails> = emptyList(),
    val cast: List<ActorMovieCastMemberEntity> = emptyList(),
    val adult: Boolean = false,
    val backdropPath: String = "",
    val character: String = "",
    val creditId: String = "",
    val genreIds: List<Int> = emptyList(),
    val id: Int = 1,
    val order: Int = 1,
    val originalLanguage: String = "",
    val originalTitle: String = "",
    val overview: String = "",
    val popularity: Double = 0.0,
    val posterPath: String = "",
    val releaseDate: String = "",
    val title: String = "",
    val video: Boolean = false,
    val voteAverage: Double = 0.0,
    val voteCount: Int = 1
)