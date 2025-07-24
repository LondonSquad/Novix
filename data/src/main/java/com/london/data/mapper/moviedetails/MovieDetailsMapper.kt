package com.london.data.mapper.moviedetails

import com.london.data.datasource.remote.details.moviedetails.model.moviedetails.MovieDetailsResponse
import com.london.data.datasource.remote.details.moviedetails.model.similarmovies.SimilarMovieRemote
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.isTrue
import com.london.data.utils.orZero
import com.london.data.utils.roundToFirstDecimal
import com.london.domain.entity.Actor
import com.london.domain.entity.Genre
import com.london.domain.entity.moviedatails.MovieDetails
import com.london.domain.entity.moviedatails.SimilarMovie

fun MovieDetailsResponse.toEntity(
    similarMovies: List<SimilarMovie> = emptyList(),
    genres: List<Genre> = emptyList(),
    actors: List<Actor> = emptyList(),
    movieImages: List<String> = emptyList(),
    movieDuration: String = ""
): MovieDetails {
    return MovieDetails(
        movieId = id.orZero(),
        movieImage = movieImages,
        movieName = title.orEmpty(),
        movieRating = voteAverage?.roundToFirstDecimal().orEmpty(),
        movieDuration = movieDuration,
        releaseDate = releaseDate.orEmpty(),
        movieOverview = overview.orEmpty(),
        genres = genres,
        actors = actors,
        similarMovies = similarMovies,
        movieHaveTrailer = video.isTrue,
    )
}


fun SimilarMovieRemote.toSimilarMovie(): SimilarMovie {
    return SimilarMovie(
        image = posterPath.asImageUrlOrEmpty(),
        isSaved = false,
        id = id.orZero()
    )
}
