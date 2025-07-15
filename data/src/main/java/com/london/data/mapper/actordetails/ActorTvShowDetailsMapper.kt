package com.london.data.mapper.actordetails

import com.london.data.datasource.remote.details.actordetails.model.ActorTvShowCastMember
import com.london.domain.entity.ActorTvShowDetails

fun ActorTvShowCastMember.toEntity(): ActorTvShowDetails {
    return ActorTvShowDetails(
        id = this.id,
        name = this.name,
        adult = this.adult,
        backdropPath = this.backdropPath,
        character = this.character,
        creditId = this.creditId,
        episodeCount = this.episodeCount,
        firstAirDate = this.firstAirDate,
        firstCreditAirDate = this.firstCreditAirDate,
        genreIds = this.genreIds,
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
