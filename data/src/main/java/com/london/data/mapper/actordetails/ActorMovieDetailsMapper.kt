package com.london.data.mapper.actordetails

import com.london.data.datasource.remote.details.actordetails.model.actormoviedetails.ActorMovieCastMember
import com.london.data.datasource.remote.details.actordetails.model.actormoviedetails.ActorMovieDetailsResponse
import com.london.data.datasource.remote.details.actordetails.model.actormoviedetails.MovieCrewMember
import com.london.data.utils.isTrue
import com.london.data.utils.orZero
import com.london.domain.KoverIgnore
import com.london.domain.entity.actordetails.actormovie.ActorMovieCastMemberEntity
import com.london.domain.entity.actordetails.actormovie.ActorMovieCrewMemberEntity
import com.london.domain.entity.actordetails.actormovie.ActorMovieDetails

fun ActorMovieDetailsResponse.toEntity(): ActorMovieDetails {
    return ActorMovieDetails(
        id = id.orZero(),
        cast = cast?.map { it.toEntity() }.orEmpty(),
        crew = crew?.map { it.toEntity() }.orEmpty()
    )
}

fun ActorMovieCastMember.toEntity(): ActorMovieCastMemberEntity {
    return ActorMovieCastMemberEntity(
        adult = adult.isTrue,
        backdropPath = "https://image.tmdb.org/t/p/w500${backdropPath}",
        character = character.orEmpty(),
        creditId = creditId.orEmpty(),
        genreIds = genreIds.orEmpty(),
        id = id.orZero(),
        order = order.orZero(),
        originalLanguage = originalLanguage.orEmpty(),
        originalTitle = originalTitle.orEmpty(),
        overview = overview.orEmpty(),
        popularity = popularity.orZero(),
        posterPath = posterPath.let { "https://image.tmdb.org/t/p/w500${it}" },
        releaseDate = releaseDate.orEmpty(),
        title = title.orEmpty(),
        video = video.isTrue,
        voteAverage = voteAverage.orZero(),
        voteCount = voteCount.orZero()
    )
}

fun MovieCrewMember.toEntity(): ActorMovieCrewMemberEntity {
    return ActorMovieCrewMemberEntity(
        adult = adult.isTrue,
        backdropPath = "https://image.tmdb.org/t/p/w500${backdropPath}",
        creditId = creditId.orEmpty(),
        department = department.orEmpty(),
        genreIds = genreIds.orEmpty(),
        id = id.orZero(),
        job = job.orEmpty(),
        originalLanguage = originalLanguage.orEmpty(),
        originalTitle = originalTitle.orEmpty(),
        overview = overview.orEmpty(),
        popularity = popularity.orZero(),
        posterPath = "https://image.tmdb.org/t/p/w500${posterPath}",
        releaseDate = releaseDate.orEmpty(),
        title = title.orEmpty(),
        video = video.isTrue,
        voteAverage = voteAverage.orZero(),
        voteCount = voteCount.orZero()
    )
}
