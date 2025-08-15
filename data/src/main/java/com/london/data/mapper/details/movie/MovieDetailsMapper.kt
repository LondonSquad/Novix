package com.london.data.mapper.details.movie

import com.london.data.remote.model.details.movie.model.moviedetails.MovieDetailsResponse
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.isTrue
import com.london.data.utils.orZero
import com.london.data.utils.roundToFirstDecimal
import com.london.domain.entity.moviedatails.MovieDetails

fun MovieDetailsResponse.toEntity(): MovieDetails = MovieDetails(
    backdropUrl = this.backdropPath.asImageUrlOrEmpty(),
    genres = this.genreRemote.orEmpty().map { it.id.orZero() }.toMovieGenre(),
    id = this.id.orZero(),
    overview = this.overview.orEmpty(),
    posterUrl = this.posterPath.asImageUrlOrEmpty(),
    releaseDate = this.releaseDate.orEmpty(),
    runtime = this.runtime.orZero(),
    title = this.title.orEmpty(),
    video = this.video.isTrue,
    voteAverage = this.voteAverage.roundToFirstDecimal(),
)
