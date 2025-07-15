package com.london.data.mapper.moviedetails

import com.london.data.datasource.remote.moviedetails.model.MovieDetailsRemote
import com.london.data.datasource.remote.moviedetails.model.SimilarMovieRemote
import com.london.domain.entity.Actor
import com.london.domain.entity.moviedatails.Genre
import com.london.domain.entity.moviedatails.MovieDetails
import com.london.domain.entity.moviedatails.SimilarMovie

fun MovieDetailsRemote.fromRemoteToDomain(
    similarMovies: List<SimilarMovie> = emptyList(),
    genres: List<Genre> = emptyList(),
    actors: List<Actor> = emptyList(),
    movieImages: List<Any> = emptyList(),
    movieDuration: String = ""
): MovieDetails {
    return MovieDetails(
        movieId = this.id,
        movieImage = movieImages,
        movieName = this.title,
        movieRating = this.voteAverage.roundToFirstDecimal(),
        movieDuration = movieDuration,
        releaseDate = this.releaseDate ?: "",
        movieOverview = this.overview,
        genres = genres,
        actors = actors,
        similarMovies = similarMovies,
        movieHaveTrailer = this.video,
    )
}


fun SimilarMovieRemote.toSimilarMovie(): SimilarMovie {
    return SimilarMovie(
        image = "https://image.tmdb.org/t/p/w500" + (this.posterPath ?: ""),
        isSaved = false,
    )
}
