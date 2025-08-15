package com.london.data.mapper.details.tvshow

import com.london.data.remote.model.details.tvshow.Role
import com.london.data.remote.model.details.tvshow.TvShowCastMember
import com.london.data.remote.model.details.tvshow.TvShowCastRemoteResponse
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.orZero
import com.london.domain.KoverIgnore
import com.london.domain.entity.tvshowdetails.TvShowCastEntity
import com.london.domain.entity.tvshowdetails.TvShowCastMemberEntity
import com.london.domain.entity.tvshowdetails.TvShowRoleEntity

@KoverIgnore
fun TvShowCastRemoteResponse.toCastEntity(): TvShowCastEntity =
    TvShowCastEntity(
        cast = cast?.map { it.toCastMember() }.orEmpty(),
        id = id
    )

fun TvShowCastMember.toCastMember(): TvShowCastMemberEntity =
    TvShowCastMemberEntity(
        id = id.orZero(),
        name = name.orEmpty(),
        profileUrl = profilePath.asImageUrlOrEmpty(),
        roles = roles?.map { it.toRoleEntity() }.orEmpty(),
    )

@KoverIgnore
fun Role.toRoleEntity(): TvShowRoleEntity =
    TvShowRoleEntity(
        character = character.orEmpty(),
        episodeCount = episodeCount
    )
