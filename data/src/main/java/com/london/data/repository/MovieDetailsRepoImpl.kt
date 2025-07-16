package com.london.data.repository

import com.london.data.datasource.remote.details.moviedetails.GetMovieCastException
import com.london.data.datasource.remote.details.moviedetails.GetMovieDetailsException
import com.london.data.datasource.remote.details.moviedetails.GetMovieImagesException
import com.london.data.datasource.remote.details.moviedetails.GetSimilarMoviesException
import com.london.data.datasource.remote.details.moviedetails.MovieDetailsRemote
import com.london.data.datasource.remote.details.moviedetails.runOrThrow
import com.london.data.mapper.moviedetails.toEntity
import com.london.data.mapper.moviedetails.toGenre
import com.london.data.mapper.moviedetails.toSimilarMovie
import com.london.domain.entity.Actor
import com.london.domain.entity.moviedatails.MovieDetails
import com.london.domain.entity.moviedatails.SimilarMovie
import com.london.domain.repository.MovieDetailsRepository

class MovieDetailsRepoImpl(
    private val movieDetailsRemote: MovieDetailsRemote,
) : MovieDetailsRepository {

    override suspend fun getMovieById(id: Int): MovieDetails =
        runOrThrow<MovieDetails>(
            block = {
                val movieDetailsRemote = movieDetailsRemote.getMovieDetails(id)
                movieDetailsRemote.toEntity(
                    genres = movieDetailsRemote.genreRemote.map { it.toGenre() },
                )
            },
            error = { cause -> GetMovieDetailsException(cause) }
        )

    override suspend fun getSimilarMoviesById(id: Int): List<SimilarMovie> =
        runOrThrow<List<SimilarMovie>>(
            block = {
                val similarMoviesRemote = movieDetailsRemote.getSimilarMovies(id)
                similarMoviesRemote.similarMovieRemotes.map { it.toSimilarMovie() }
            },
            error = { cause -> GetSimilarMoviesException(cause) }
        )

    override suspend fun getMovieImagesById(id: Int): List<String> =
        runOrThrow<List<String>>(
            block = {
                val images = movieDetailsRemote.getMovieImages(id)
                when {
                    images.backdrops.isNotEmpty() -> images.backdrops.map { it.filePath }
                    images.posters.isNotEmpty() -> images.posters.map { it.filePath }
                    images.logos.isNotEmpty() -> images.logos.map { it.filePath }
                    else -> emptyList()
                }.take(IMAGE_LIMIT)
            },
            error = { cause -> GetMovieImagesException(cause) }
        )

    override suspend fun getMovieCastById(id: Int): List<Actor> =
        runOrThrow<List<Actor>>(
            block = {
                val movieCast = movieDetailsRemote.getMovieCast(id)
                movieCast.actorRemote.map { it.toEntity() }
            },
            error = { cause -> GetMovieCastException(cause) }
        )
}

const val IMAGE_LIMIT = 10