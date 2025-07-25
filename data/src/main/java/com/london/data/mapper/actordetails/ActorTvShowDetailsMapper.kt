@file:KoverIgnore
package com.london.data.mapper.actordetails

import com.london.data.remote.model.details.actor.model.actortvshowdetails.ActorTvShowCastMember
import com.london.data.remote.model.details.actor.model.actortvshowdetails.ActorTvShowCrewMember
import com.london.data.remote.model.details.actor.model.actortvshowdetails.ActorTvShowDetailsResponse
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.isTrue
import com.london.data.utils.orZero
import com.london.domain.KoverIgnore
import com.london.domain.entity.actordetails.actortvshow.ActorTvShowCastMemberEntity
import com.london.domain.entity.actordetails.actortvshow.ActorTvShowCrewMemberEntity
import com.london.domain.entity.actordetails.actortvshow.ActorTvShowDetails

fun ActorTvShowDetailsResponse.toEntity(): ActorTvShowDetails {
    return ActorTvShowDetails(
        id = id.orZero(),
        cast = cast?.map { it.toEntity() }.orEmpty(),
        crew = crew?.map { it.toEntity() }.orEmpty()
    )
}

fun ActorTvShowCastMember.toEntity(): ActorTvShowCastMemberEntity {
    return ActorTvShowCastMemberEntity(
        adult = adult.isTrue,
        backdropUrl = backdropPath.asImageUrlOrEmpty(),
        character = character.orEmpty(),
        creditId = creditId.orEmpty(),
        episodeCount = episodeCount.orZero(),
        firstAirDate = firstAirDate.orEmpty(),
        firstCreditAirDate = firstCreditAirDate.orEmpty(),
        genreIds = genreIds.orEmpty(),
        id = id.orZero(),
        name = name.orEmpty(),
        originCountry = originCountry.orEmpty(),
        originalLanguage = originalLanguage.orEmpty(),
        originalName = originalName.orEmpty(),
        overview = overview.orEmpty(),
        popularity = popularity.orZero(),
        posterUrl = posterPath.asImageUrlOrEmpty(),
        voteAverage = voteAverage.orZero(),
        voteCount = voteCount.orZero()
    )
}

fun ActorTvShowCrewMember.toEntity(): ActorTvShowCrewMemberEntity{
    return ActorTvShowCrewMemberEntity(
        adult = adult.isTrue,
        backdropUrl = backdropPath.asImageUrlOrEmpty(),
        creditId = creditId.orEmpty(),
        department = department.orEmpty(),
        episodeCount = episodeCount.orZero(),
        firstAirDate = firstAirDate.orEmpty(),
        firstCreditAirDate = firstCreditAirDate.orEmpty(),
        genreIds = genreIds.orEmpty(),
        id = id.orZero(),
        job = job.orEmpty(),
        name = name.orEmpty(),
        originCountry = originCountry.orEmpty(),
        originalLanguage = originalLanguage.orEmpty(),
        originalName = originalName.orEmpty(),
        overview = overview.orEmpty(),
        popularity = popularity.orZero(),
        posterUrl = posterPath.asImageUrlOrEmpty(),
        voteAverage = voteAverage.orZero(),
        voteCount = voteCount.orZero()
    )
}
