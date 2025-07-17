@file:KoverIgnore
package com.london.data.mapper.actordetails

import com.london.data.datasource.remote.details.actordetails.model.actortvshowdetails.ActorTvShowCastMember
import com.london.data.datasource.remote.details.actordetails.model.actortvshowdetails.ActorTvShowCrewMember
import com.london.data.datasource.remote.details.actordetails.model.actortvshowdetails.ActorTvShowDetailsResponse
import com.london.data.utils.isTrue
import com.london.data.utils.orZero
import com.london.domain.KoverIgnore
import com.london.domain.entity.actordetails.actortvshow.ActorTvShowCastMemberEntity
import com.london.domain.entity.actordetails.actortvshow.ActorTvShowCrewMemberEntity
import com.london.domain.entity.actordetails.actortvshow.ActorTvShowDetails

fun ActorTvShowDetailsResponse.toEntity(): ActorTvShowDetails {
    return ActorTvShowDetails(
        id = this.id.orZero(),
        cast = this.cast?.map { it.toEntity() }.orEmpty(),
        crew = this.crew?.map { it.toEntity() }.orEmpty()
    )
}

fun ActorTvShowCastMember.toEntity(): ActorTvShowCastMemberEntity {
    return ActorTvShowCastMemberEntity(
        adult = this.adult.isTrue,
        backdropUrl = "https://image.tmdb.org/t/p/w500${this.backdropPath}",
        character = this.character.orEmpty(),
        creditId = this.creditId.orEmpty(),
        episodeCount = this.episodeCount.orZero(),
        firstAirDate = this.firstAirDate.orEmpty(),
        firstCreditAirDate = this.firstCreditAirDate.orEmpty(),
        genreIds = this.genreIds.orEmpty(),
        id = this.id.orZero(),
        name = this.name.orEmpty(),
        originCountry = this.originCountry.orEmpty(),
        originalLanguage = this.originalLanguage.orEmpty(),
        originalName = this.originalName.orEmpty(),
        overview = this.overview.orEmpty(),
        popularity = this.popularity.orZero(),
        posterUrl = "https://image.tmdb.org/t/p/w500${this.posterPath}",
        voteAverage = this.voteAverage.orZero(),
        voteCount = this.voteCount.orZero()
    )
}

fun ActorTvShowCrewMember.toEntity(): ActorTvShowCrewMemberEntity{
    return ActorTvShowCrewMemberEntity(
        adult = this.adult.isTrue,
        backdropPath = "https://image.tmdb.org/t/p/w500${this.backdropPath}",
        creditId = this.creditId.orEmpty(),
        department = this.department.orEmpty(),
        episodeCount = this.episodeCount.orZero(),
        firstAirDate = this.firstAirDate.orEmpty(),
        firstCreditAirDate = this.firstCreditAirDate.orEmpty(),
        genreIds = this.genreIds.orEmpty(),
        id = this.id.orZero(),
        job = this.job.orEmpty(),
        name = this.name.orEmpty(),
        originCountry = this.originCountry.orEmpty(),
        originalLanguage = this.originalLanguage.orEmpty(),
        originalName = this.originalName.orEmpty(),
        overview = this.overview.orEmpty(),
        popularity = this.popularity.orZero(),
        posterPath = "https://image.tmdb.org/t/p/w500${this.posterPath}",
        voteAverage = this.voteAverage.orZero(),
        voteCount = this.voteCount.orZero()
    )
}
