package com.london.data.mapper.actordetails

import com.london.data.datasource.remote.details.actordetails.model.actortvshowdetails.ActorTvShowCastMember
import com.london.data.datasource.remote.details.actordetails.model.actortvshowdetails.ActorTvShowCrewMember
import com.london.data.datasource.remote.details.actordetails.model.actortvshowdetails.ActorTvShowDetailsResponse
import com.london.domain.entity.actordetails.actortvshow.ActorTvShowCastMemberEntity
import com.london.domain.entity.actordetails.actortvshow.ActorTvShowCrewMemberEntity
import com.london.domain.entity.actordetails.actortvshow.ActorTvShowDetails

fun ActorTvShowDetailsResponse.toEntity(): ActorTvShowDetails {
    return ActorTvShowDetails(
        id = this.id,
        cast = this.cast.map { it.toEntity() },
        crew = this.crew.map { it.toEntity() }
    )
}

fun ActorTvShowCastMember.toEntity(): ActorTvShowCastMemberEntity {
    return ActorTvShowCastMemberEntity(
        adult = this.adult,
        backdropPath = this.backdropPath,
        character = this.character,
        creditId = this.creditId,
        episodeCount = this.episodeCount,
        firstAirDate = this.firstAirDate,
        firstCreditAirDate = this.firstCreditAirDate,
        genreIds = this.genreIds,
        id = this.id,
        name = this.name,
        originCountry = this.originCountry,
        originalLanguage = this.originalLanguage,
        originalName = this.originalName,
        overview = this.overview,
        popularity = this.popularity,
        posterPath = this.posterPath,
        voteAverage = this.voteAverage,
        voteCount = this.voteCount
    )
}

fun ActorTvShowCrewMember.toEntity(): ActorTvShowCrewMemberEntity{
    return ActorTvShowCrewMemberEntity(
        adult = this.adult,
        backdropPath = this.backdropPath,
        creditId = this.creditId,
        department = this.department,
        episodeCount = this.episodeCount,
        firstAirDate = this.firstAirDate,
        firstCreditAirDate = this.firstCreditAirDate,
        genreIds = this.genreIds,
        id = this.id,
        job = this.job,
        name = this.name,
        originCountry = this.originCountry,
        originalLanguage = this.originalLanguage,
        originalName = this.originalName,
        overview = this.overview,
        popularity = this.popularity,
        posterPath = this.posterPath,
        voteAverage = this.voteAverage,
        voteCount = this.voteCount
    )
}
