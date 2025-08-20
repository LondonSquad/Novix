package com.london.domain.entity.tvshow.cast

data class TvShowCastMember(
    val id: Int,
    val name: String,
    val profileUrl: String?,
    val roles: List<TvShowRole>,
)
