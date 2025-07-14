package com.london.data.mapper.tvshowdetails

import com.london.data.datasource.remote.cast.model.CastMember
import com.london.data.datasource.remote.cast.model.CastRemoteResponse
import com.london.data.datasource.remote.cast.model.CrewMember
import com.london.domain.entity.tvshowdetails.CastEntity
import com.london.domain.entity.tvshowdetails.CastMemberEntity
import com.london.domain.entity.tvshowdetails.CrewMemberEntity

fun CastRemoteResponse.toCastEntity(): CastEntity {
    return CastEntity(
        cast = this.cast.map { it.toCastMember() },
        crew = this.crew.map { it.toCrewMember() },
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
        profilePath = "https://image.tmdb.org/t/p/w500${this.profilePath}",
        character = this.character,
        creditId = this.creditId,
        order = this.order
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
        profilePath = "https://image.tmdb.org/t/p/w500${this.profilePath}",
        creditId = this.creditId,
        department = this.department,
        job = this.job
    )
}