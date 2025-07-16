package com.london.data.mapper.tvshowdetails

import com.london.data.datasource.remote.details.tvshowdetails.model.CrewMember
import com.london.data.datasource.remote.details.tvshowdetails.model.Role
import com.london.data.datasource.remote.details.tvshowdetails.model.TvShowCastMember
import com.london.data.datasource.remote.details.tvshowdetails.model.TvShowCastRemoteResponse
import com.london.domain.entity.tvshowdetails.RoleEntity
import com.london.domain.entity.tvshowdetails.TvShowCastEntity
import com.london.domain.entity.tvshowdetails.TvShowCastMemberEntity
import com.london.domain.entity.tvshowdetails.TvShowCrewMemberEntity

fun TvShowCastRemoteResponse.toCastEntity(): TvShowCastEntity {
    return TvShowCastEntity(
        cast = this.cast.map { it.toCastMember() },
        id = this.id
    )
}

fun TvShowCastMember.toCastMember(): TvShowCastMemberEntity {
    return TvShowCastMemberEntity(
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

fun CrewMember.toCrewMember(): TvShowCrewMemberEntity {
    return TvShowCrewMemberEntity(
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