package com.london.domain.entity.tvshowdetails.episode

data class TvShowEpisodeBySeasonEntity(
    val airDate: String?,
    val episodeNumber: Int,
    val episodeType: String,
    val id: Int,
    val name: String,
    val overview: String,
    val productionCode: String,
    val runtime: Int?,
    val seasonNumber: Int,
    val showId: Int,
    val stillPath: String?,
    val voteAverage: Double,
    val voteCount: Int,
    val crew: List<EpisodeCrewMemberEntity>,
    val episodeGuestStars: List<EpisodeGuestStarEntity>
)
