package com.london.data.mapper.moviedetails

import com.london.data.remote.model.details.movie.model.moviedetails.MovieDetailsResponse
import com.london.data.utils.orZero
import com.london.domain.entity.moviedatails.MovieDetails

fun MovieDetailsResponse.toEntity(): MovieDetails {
    return MovieDetails(
        backdropUrl = this.backdropPath.orEmpty(),
        genresId = this.genreRemote?.map { it.id.orZero() }.orEmpty(),
        id = this.id.orZero(),
        overview = this.overview.orEmpty(),
        posterUrl = this.posterPath.orEmpty(),
        releaseDate = this.releaseDate.orEmpty(),
        runtime = this.runtime.orZero(),
        title = this.title.orEmpty(),
        video = this.video == true,
        voteAverage = this.voteAverage.orZero().toString(),
    )
}


