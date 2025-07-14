package com.london.domain.entity.tvshowdetails

data class CastEntity(
    val cast: List<CastMemberEntity>,
    val crew: List<CrewMemberEntity>,
    val id: Int
)
