package com.london.data.mapper.actordetails

import com.london.data.remote.model.details.actor.model.actormoviedetails.ActorMovieCastMember
import com.london.data.remote.model.details.actor.model.actormoviedetails.ActorMovieDetailsResponse
import com.london.data.remote.model.details.actor.model.actormoviedetails.MovieCrewMember
import com.london.data.utils.asImageUrlOrEmpty
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

@KoverIgnore
fun ActorMovieCastMember.toEntity(): ActorMovieCastMemberEntity {
    return ActorMovieCastMemberEntity(
        adult = adult.isTrue,
        backdropUrl = backdropPath.asImageUrlOrEmpty(),
        character = character.orEmpty(),
        creditId = creditId.orEmpty(),
        genreIds = genreIds.orEmpty(),
        id = id.orZero(),
        order = order.orZero(),
        originalLanguage = originalLanguage.orEmpty(),
        originalTitle = originalTitle.orEmpty(),
        overview = overview.orEmpty(),
        popularity = popularity.orZero(),
        posterUrl = posterPath.asImageUrlOrEmpty(),
        releaseDate = releaseDate.orEmpty(),
        title = title.orEmpty(),
        video = video.isTrue,
        voteAverage = voteAverage.orZero(),
        voteCount = voteCount.orZero()
    )
}

@KoverIgnore
fun MovieCrewMember.toEntity(): ActorMovieCrewMemberEntity {
    return ActorMovieCrewMemberEntity(
        adult = adult.isTrue,
        backdropUrl = backdropPath.asImageUrlOrEmpty(),
        creditId = creditId.orEmpty(),
        department = department.orEmpty(),
        genreIds = genreIds.orEmpty(),
        id = id.orZero(),
        job = job.orEmpty(),
        originalLanguage = originalLanguage.orEmpty(),
        originalTitle = originalTitle.orEmpty(),
        overview = overview.orEmpty(),
        popularity = popularity.orZero(),
        posterUrl = posterPath.asImageUrlOrEmpty(),
        releaseDate = releaseDate.orEmpty(),
        title = title.orEmpty(),
        video = video.isTrue,
        voteAverage = voteAverage.orZero(),
        voteCount = voteCount.orZero()
    )
}
