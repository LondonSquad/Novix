package com.london.data.mapper.tvshowdetails

import com.london.data.remote.model.details.tvshow.model.Role
import com.london.data.remote.model.details.tvshow.model.TvShowCastMember
import com.london.data.remote.model.details.tvshow.model.TvShowCastRemoteResponse
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.isTrue
import com.london.data.utils.orZero
import com.london.domain.KoverIgnore
import com.london.domain.entity.tvshowdetails.TvShowCastEntity
import com.london.domain.entity.tvshowdetails.TvShowCastMemberEntity
import com.london.domain.entity.tvshowdetails.TvShowRoleEntity

@KoverIgnore
fun TvShowCastRemoteResponse.toCastEntity(): TvShowCastEntity {
    return TvShowCastEntity(
        cast = cast?.map { it.toCastMember() }.orEmpty(),
        id = id
    )
}

fun TvShowCastMember.toCastMember(): TvShowCastMemberEntity {
    return TvShowCastMemberEntity(
        adult = adult.isTrue,
        gender = gender.orZero(),
        id = id.orZero(),
        knownForDepartment = knownForDepartment.orEmpty(),
        name = name.orEmpty(),
        originalName = originalName.orEmpty(),
        popularity = popularity.orZero(),
        profileUrl = profilePath.asImageUrlOrEmpty(),
        roles = roles?.map { it.toRoleEntity() }.orEmpty(),
        totalEpisodeCount = totalEpisodeCount.orZero(),
        order = order.orZero()
    )
}

@KoverIgnore
fun Role.toRoleEntity(): TvShowRoleEntity {
    return TvShowRoleEntity(
        creditId = creditId.orEmpty(),
        character = character.orEmpty(),
        episodeCount = episodeCount
    )
}
