package com.london.data.remote.model.details.tvshow.model.tvshowepisode

import com.london.domain.KoverIgnore
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@KoverIgnore
@Serializable
data class EpisodeResponse(
    @SerialName("air_date")
    val airDate: String? = null,
    @SerialName("episode_type")
    val episodeType: String? = null,
    @SerialName("id")
    val id: Int? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("overview")
    val overview: String? = null,
    @SerialName("season_number")
    val seasonNumber: Int? = null,
    @SerialName("still_path")
    val stillPath: String? = null,
    @SerialName("vote_average")
    val voteAverage: Double? = null,
    @SerialName("vote_count")
    val voteCount: Int? = null,
    @SerialName("guest_stars")
    val guestStars: List<EpisodeGuestStar>? = null  ,
)
