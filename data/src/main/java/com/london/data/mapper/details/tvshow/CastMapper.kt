package com.london.data.mapper.details.tvshow

import com.london.data.remote.model.details.tvshow.model.Role
import com.london.data.remote.model.details.tvshow.model.TvShowCastMemberResponse
import com.london.data.remote.model.details.tvshow.model.TvShowCastRemoteResponse
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.orZero
import com.london.domain.KoverIgnore
import com.london.domain.entity.tvshow.cast.TvShowCast
import com.london.domain.entity.tvshow.cast.TvShowCastMember
import com.london.domain.entity.tvshow.cast.TvShowRole

@KoverIgnore
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

@KoverIgnore
fun Role.toRoleEntity(): TvShowRole =
    TvShowRole(
        character = character.orEmpty(),
        episodeCount = episodeCount.orZero()
    )
