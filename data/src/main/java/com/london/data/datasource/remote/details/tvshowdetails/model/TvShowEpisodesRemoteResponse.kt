package com.london.data.datasource.remote.details.tvshowdetails.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TvShowEpisodesRemoteResponse(
    @SerialName("_id")
    val id: String,
    @SerialName("air_date")
    val airDate: String?,
    @SerialName("episodes")
    val episodes: List<TvShowEpisodeBySeason>
)

@Serializable
data class TvShowEpisodeBySeason(
    @SerialName("air_date")
    val airDate: String?,
    @SerialName("episode_number")
    val episodeNumber: Int,
    @SerialName("episode_type")
    val episodeType: String,
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("overview")
    val overview: String,
    @SerialName("production_code")
    val productionCode: String,
    @SerialName("runtime")
    val runtime: Int?,
    @SerialName("season_number")
    val seasonNumber: Int,
    @SerialName("show_id")
    val showId: Int,
    @SerialName("still_path")
    val stillPath: String?,
    @SerialName("vote_average")
    val voteAverage: Double,
    @SerialName("vote_count")
    val voteCount: Int,
    @SerialName("crew")
    val crew: List<EpisodeCrewMember>,
    @SerialName("guest_stars")
    val episodeGuestStars: List<EpisodeGuestStar>
)

@Serializable
data class EpisodeCrewMember(
    @SerialName("job")
    val job: String,
    @SerialName("department")
    val department: String,
    @SerialName("credit_id")
    val creditId: String,
    @SerialName("adult")
    val adult: Boolean,
    @SerialName("gender")
    val gender: Int,
    @SerialName("id")
    val id: Int,
    @SerialName("known_for_department")
    val knownForDepartment: String,
    @SerialName("name")
    val name: String,
    @SerialName("original_name")
    val originalName: String,
    @SerialName("popularity")
    val popularity: Double,
    @SerialName("profile_path")
    val profilePath: String?
)

@Serializable
data class EpisodeGuestStar(
    @SerialName("character")
    val character: String,
    @SerialName("credit_id")
    val creditId: String,
    @SerialName("order")
    val order: Int,
    @SerialName("adult")
    val adult: Boolean,
    @SerialName("gender")
    val gender: Int,
    @SerialName("id")
    val id: Int,
    @SerialName("known_for_department")
    val knownForDepartment: String,
    @SerialName("name")
    val name: String,
    @SerialName("original_name")
    val originalName: String,
    @SerialName("popularity")
    val popularity: Double,
    @SerialName("profile_path")
    val profilePath: String?
)