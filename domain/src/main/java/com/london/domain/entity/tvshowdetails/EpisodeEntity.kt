package com.london.domain.entity.tvshowdetails

data class TvShowEpisodesEntity(
    val id: String,
    val airDate: String?,
    val episodes: List<TvShowEpisodeBySeasonEntity>
)

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

data class EpisodeCrewMemberEntity(
    val job: String,
    val department: String,
    val creditId: String,
    val adult: Boolean,
    val gender: Int,
    val id: Int,
    val knownForDepartment: String,
    val name: String,
    val originalName: String,
    val popularity: Double,
    val profilePath: String?
)

data class EpisodeGuestStarEntity(
    val character: String,
    val creditId: String,
    val order: Int,
    val adult: Boolean,
    val gender: Int,
    val id: Int,
    val knownForDepartment: String,
    val name: String,
    val originalName: String,
    val popularity: Double,
    val profilePath: String?
)