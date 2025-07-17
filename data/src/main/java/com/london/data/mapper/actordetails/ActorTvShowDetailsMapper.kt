@file:KoverIgnore

package com.london.data.mapper.actordetails

import com.london.data.datasource.remote.details.actordetails.model.actortvshowdetails.ActorTvShowCastMember
import com.london.data.datasource.remote.details.actordetails.model.actortvshowdetails.ActorTvShowCrewMember
import com.london.data.datasource.remote.details.actordetails.model.actortvshowdetails.ActorTvShowDetailsResponse
import com.london.data.utils.asImageUrlOrEmpty
import com.london.domain.KoverIgnore
import com.london.domain.entity.actordetails.actortvshow.ActorTvShowCastMemberEntity
import com.london.domain.entity.actordetails.actortvshow.ActorTvShowCrewMemberEntity
import com.london.domain.entity.actordetails.actortvshow.ActorTvShowDetails

fun ActorTvShowDetailsResponse.toEntity(): ActorTvShowDetails {
    return ActorTvShowDetails(
        id = id,
        cast = cast.map { it.toEntity() },
        crew = crew.map { it.toEntity() }
    )
}

fun ActorTvShowCastMember.toEntity(): ActorTvShowCastMemberEntity {
    return ActorTvShowCastMemberEntity(
        adult = adult,
        backdropPath = backdropPath.asImageUrlOrEmpty(),
        character = character,
        creditId = creditId,
        episodeCount = episodeCount,
        firstAirDate = firstAirDate,
        firstCreditAirDate = firstCreditAirDate,
        genreIds = genreIds,
        id = id,
        name = name,
        originCountry = originCountry,
        originalLanguage = originalLanguage,
        originalName = originalName,
        overview = overview,
        popularity = popularity,
        posterPath = posterPath.asImageUrlOrEmpty(),
        voteAverage = voteAverage,
        voteCount = voteCount
    )
}

fun ActorTvShowCrewMember.toEntity(): ActorTvShowCrewMemberEntity {
    return ActorTvShowCrewMemberEntity(
        adult = adult,
        backdropPath = backdropPath.asImageUrlOrEmpty(),
        creditId = creditId,
        department = department,
        episodeCount = episodeCount,
        firstAirDate = firstAirDate,
        firstCreditAirDate = firstCreditAirDate,
        genreIds = genreIds,
        id = id,
        job = job,
        name = name,
        originCountry = originCountry,
        originalLanguage = originalLanguage,
        originalName = originalName,
        overview = overview,
        popularity = popularity,
        posterPath = backdropPath.asImageUrlOrEmpty(),
        voteAverage = voteAverage,
        voteCount = voteCount
    )
}
