package com.london.data.mapper.actordetails

import com.london.data.datasource.remote.details.actordetails.model.actormoviedetails.ActorMovieCastMember
import com.london.data.datasource.remote.details.actordetails.model.actormoviedetails.ActorMovieDetailsResponse
import com.london.data.datasource.remote.details.actordetails.model.actormoviedetails.MovieCrewMember
import com.london.domain.entity.actordetails.actormovie.ActorMovieCastMemberEntity
import com.london.domain.entity.actordetails.actormovie.ActorMovieCrewMemberEntity
import com.london.domain.entity.actordetails.actormovie.ActorMovieDetails

fun ActorMovieDetailsResponse.toEntity(): ActorMovieDetails {
    return ActorMovieDetails(
        id = this.id,
        cast = this.cast.map { it.toEntity() },
        crew = this.crew.map { it.toEntity() }
    )
}
fun ActorMovieCastMember.toEntity(): ActorMovieCastMemberEntity {
    return ActorMovieCastMemberEntity(
        adult = this.adult,
        backdropPath = this.backdropPath,
        character = this.character,
        creditId = this.creditId,
        genreIds = this.genreIds,
        id = this.id,
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
fun MovieCrewMember.toEntity(): ActorMovieCrewMemberEntity {
    return ActorMovieCrewMemberEntity(
        adult = this.adult,
        backdropPath = this.backdropPath,
        creditId = this.creditId,
        department = this.department,
        genreIds = this.genreIds,
        id = this.id,
        job = this.job,
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