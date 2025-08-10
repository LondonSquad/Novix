package com.london.domain.entity.tvshowdetails

data class TvShowCastEntity(
    val cast: List<TvShowCastMemberEntity>,
    val id: Int?
)

data class TvShowCastMemberEntity(
    val id: Int,
    val name: String,
    val profileUrl: String?,
    val roles: List<TvShowRoleEntity>,
)

data class TvShowRoleEntity(
    val character: String,
    val episodeCount: Int
)