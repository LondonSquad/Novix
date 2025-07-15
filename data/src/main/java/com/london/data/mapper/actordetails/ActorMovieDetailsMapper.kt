package com.london.data.mapper.actordetails

import com.london.data.datasource.remote.details.actordetails.model.ActorMovieCastMember
import com.london.domain.entity.ActorMovieDetails

fun ActorMovieCastMember.toEntity(): ActorMovieDetails {
    return ActorMovieDetails(
        id = this.id,
        adult = this.adult,
        backdropPath = this.backdropPath,
        character = this.character,
        creditId = this.creditId,
        genreIds = this.genreIds,
        order = this.order,
        originalLanguage = this.originalLanguage,
        originalTitle = this.originalTitle,
        overview = this.overview,
        popularity = this.popularity,
        posterPath = this.posterPath,
        releaseDate = this.releaseDate,
        title = this.title,
        video = this.video,
        voteAverage = this.voteAverage,
        voteCount = this.voteCount
    )
}