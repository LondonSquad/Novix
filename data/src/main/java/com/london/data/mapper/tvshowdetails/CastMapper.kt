package com.london.data.mapper.tvshowdetails

import com.london.data.datasource.remote.details.tvshowdetails.model.Role
import com.london.data.datasource.remote.details.tvshowdetails.model.TvShowCastMember
import com.london.data.datasource.remote.details.tvshowdetails.model.TvShowCastRemoteResponse
import com.london.data.utils.asImageUrlOrEmpty
import com.london.domain.KoverIgnore
import com.london.domain.entity.tvshowdetails.TvShowCastEntity
import com.london.domain.entity.tvshowdetails.TvShowCastMemberEntity
import com.london.domain.entity.tvshowdetails.TvShowRoleEntity

@KoverIgnore
fun TvShowCastRemoteResponse.toCastEntity(): TvShowCastEntity {
    return TvShowCastEntity(
        cast = cast.map { it.toCastMember() },
        id = id
    )
}

fun TvShowCastMember.toCastMember(): TvShowCastMemberEntity {
    return TvShowCastMemberEntity(
        adult = adult,
        gender = gender,
        id = id,
        knownForDepartment = knownForDepartment,
        name = name,
        originalName = originalName,
        popularity = popularity,
        profileUrl = profilePath.asImageUrlOrEmpty(),
        roles = roles.map { it.toRoleEntity() },
        totalEpisodeCount = totalEpisodeCount,
        order = order
    )
}

@KoverIgnore
fun Role.toRoleEntity(): TvShowRoleEntity {
    return TvShowRoleEntity(
        creditId = creditId,
        character = character,
        episodeCount = episodeCount
    )
}
