package com.london.data.mapper.tvshowdetails

import com.london.data.datasource.remote.details.tvshowdetails.model.TvShowCastMember
import com.london.data.datasource.remote.details.tvshowdetails.model.TvShowCastRemoteResponse
import com.london.data.datasource.remote.details.tvshowdetails.model.CrewMember
import com.london.domain.entity.tvshowdetails.TvShowCastEntity
import com.london.domain.entity.tvshowdetails.TvShowCastMemberEntity
import com.london.domain.entity.tvshowdetails.TvShowCrewMemberEntity

fun TvShowCastRemoteResponse.toCastEntity(): TvShowCastEntity {
    return TvShowCastEntity(
        cast = this.cast.map { it.toCastMember() },
        crew = this.crew.map { it.toCrewMember() },
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
        profilePath = "https://image.tmdb.org/t/p/w500${this.profilePath}",
        character = this.character,
        creditId = this.creditId,
        order = this.order
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
        profilePath = "https://image.tmdb.org/t/p/w500${this.profilePath}",
        creditId = this.creditId,
        department = this.department,
        job = this.job
    )
}