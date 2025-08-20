package com.london.data.mapper.details.tvshow

import com.london.data.remote.model.details.tvshow.Role
import com.london.data.remote.model.details.tvshow.TvShowCastMemberResponse
import com.london.data.remote.model.details.tvshow.TvShowCastRemoteResponse
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.orZero
import com.london.domain.entity.tvshow.cast.TvShowCast
import com.london.domain.entity.tvshow.cast.TvShowCastMember
import com.london.domain.entity.tvshow.cast.TvShowRole

fun TvShowCastRemoteResponse.toCastEntity(): TvShowCast =
    TvShowCast(
        cast = cast?.map { it.toCastMember() }.orEmpty(),
        id = id
    )

fun TvShowCastMemberResponse.toCastMember(): TvShowCastMember =
    TvShowCastMember(
        id = id.orZero(),
        name = name.orEmpty(),
        profileUrl = profilePath.asImageUrlOrEmpty(),
        roles = roles?.map { it.toRoleEntity() }.orEmpty(),
    )

fun Role.toRoleEntity(): TvShowRole =
    TvShowRole(
        character = character.orEmpty(),
        episodeCount = episodeCount.orZero()
    )
