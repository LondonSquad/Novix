package com.london.domain.entity.tvshowdetails

data class TvShowCastEntity(
    val cast: List<TvShowCastMemberEntity>,
    val id: Int?
)

data class TvShowCastMemberEntity(
    val adult: Boolean,
    val gender: Int,
    val id: Int,
    val knownForDepartment: String,
    val name: String,
    val originalName: String,
    val popularity: Double,
    val profileUrl: String?,
    val roles: List<TvShowRoleEntity>,
    val totalEpisodeCount: Int,
    val order: Int
)

data class TvShowRoleEntity(
    val creditId: String,
    val character: String,
    val episodeCount: Int
)

data class TvShowCrewMemberEntity(
    val adult: Boolean,
    val gender: Int,
    val id: Int,
    val knownForDepartment: String,
    val name: String,
    val originalName: String,
    val popularity: Double,
    val profileUrl: String?,
    val creditId: String,
    val department: String,
    val job: String
)