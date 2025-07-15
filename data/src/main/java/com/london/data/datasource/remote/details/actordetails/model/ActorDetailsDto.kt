package com.london.data.datasource.remote.details.actordetails.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActorDetailsDto(
    @SerialName("adult")
    val adult: Boolean,

    @SerialName("also_known_as")
    val alsoKnownAs: List<String>,

    @SerialName("biography")
    val biography: String,

    @SerialName("birthday")
    val birthday: String,

    @SerialName("deathday")
    val deathDay: String? = null,

    @SerialName("gender")
    val gender: Int,

    @SerialName("homepage")
    val homePage: String? = null,

    @SerialName("id")
    val id: Int,

    @SerialName("imdb_id")
    val imdbId: String,

    @SerialName("known_for_department")
    val knownForDepartment: String,

    @SerialName("name")
    val name: String,

    @SerialName("place_of_birth")
    val placeOfBirth: String,

    @SerialName("popularity")
    val popularity: Double,

    @SerialName("profile_path")
    val profilePath: String
)
