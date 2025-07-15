package com.london.data.mapper.tvshowdetails

import com.london.data.datasource.remote.details.tvshowdetails.model.CastMember
import com.london.data.datasource.remote.details.tvshowdetails.model.TvShowCastRemoteResponse
import com.london.data.datasource.remote.details.tvshowdetails.model.CrewMember
import com.london.data.datasource.remote.details.tvshowdetails.model.Role
import com.london.domain.entity.tvshowdetails.CastEntity
import com.london.domain.entity.tvshowdetails.CastMemberEntity
import com.london.domain.entity.tvshowdetails.CrewMemberEntity
import com.london.domain.entity.tvshowdetails.RoleEntity

fun TvShowCastRemoteResponse.toCastEntity(): CastEntity {
    return CastEntity(
        cast = this.cast.map { it.toCastMember() },
        id = this.id
    )
}

fun CastMember.toCastMember(): CastMemberEntity {
    return CastMemberEntity(
        adult = this.adult,
        gender = this.gender,
        id = this.id,
        knownForDepartment = this.knownForDepartment,
        name = this.name,
        originalName = this.originalName,
        popularity = this.popularity,
        profilePath = this.profilePath?.let { "https://image.tmdb.org/t/p/w500$it" },
        roles = this.roles.map { it.toRoleEntity() },
        totalEpisodeCount = this.totalEpisodeCount,
        order = this.order
    )
}

fun Role.toRoleEntity(): RoleEntity {
    return RoleEntity(
        creditId = this.creditId,
        character = this.character,
        episodeCount = this.episodeCount
    )
}

fun CrewMember.toCrewMember(): CrewMemberEntity {
    return CrewMemberEntity(
        adult = this.adult,
        gender = this.gender,
        id = this.id,
        knownForDepartment = this.knownForDepartment,
        name = this.name,
        originalName = this.originalName,
        popularity = this.popularity,
        profilePath = this.profilePath?.let { "https://image.tmdb.org/t/p/w500$it" },
        creditId = this.creditId,
        department = this.department,
        job = this.job
    )
}