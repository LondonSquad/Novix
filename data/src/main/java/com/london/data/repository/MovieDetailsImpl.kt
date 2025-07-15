package com.london.data.repository

import com.london.data.datasource.remote.moviedetails.GetMovieCastException
import com.london.data.datasource.remote.moviedetails.GetMovieDetailsException
import com.london.data.datasource.remote.moviedetails.GetMovieImagesException
import com.london.data.datasource.remote.moviedetails.GetSimilarMoviesException
import com.london.data.datasource.remote.moviedetails.MovieDetailsRemoteDataSource
import com.london.data.mapper.moviedetails.fromRemoteToDomain
import com.london.data.mapper.moviedetails.toActor
import com.london.data.mapper.moviedetails.toGenre
import com.london.data.mapper.moviedetails.toSimilarMovie
import com.london.data.datasource.remote.moviedetails.runOrThrowHandler
import com.london.domain.entity.Actor
import com.london.domain.entity.moviedatails.MovieDetails
import com.london.domain.entity.moviedatails.SimilarMovie
import com.london.domain.repository.MovieDetailsRepository

class MovieDetailsImpl(
    private val movieDetailsRemoteDataSource: MovieDetailsRemoteDataSource,
) : MovieDetailsRepository {
    override suspend fun getMovieUsingId(id: Int): MovieDetails = runOrThrowHandler(block = {
        val movieDetailsRemote = movieDetailsRemoteDataSource.getMovieDetails(id)
        movieDetailsRemote.fromRemoteToDomain(
            genres = movieDetailsRemote.genreRemotes.map { it.toGenre() },
        )
    }, error = { cause -> GetMovieDetailsException(cause) })

    override suspend fun getSimilarMoviesUsingId(id: Int): List<SimilarMovie> = runOrThrowHandler({
        val similarMoviesRemote = movieDetailsRemoteDataSource.getSimilarMovies(id)
        similarMoviesRemote.similarMovieRemotes.map { it.toSimilarMovie() }
    }, error = { cause -> GetSimilarMoviesException(cause) })

    override suspend fun getMovieImagesUsingId(id: Int): List<String> = runOrThrowHandler(block = {
        val images = movieDetailsRemoteDataSource.getMovieImages(id)
        when {
            images.backdrops.isNotEmpty() -> images.backdrops.map { it.filePath }
            images.posters.isNotEmpty() -> images.posters.map { it.filePath }
            images.logos.isNotEmpty() -> images.logos.map { it.filePath }
            else -> emptyList()
        }.take(IMAGE_LIMIT)
    }, error = { cause -> GetMovieImagesException(cause) })

    override suspend fun getMovieCastUsingId(id: Int): List<Actor> = runOrThrowHandler(block = {
        val movieCast = movieDetailsRemoteDataSource.getMovieCast(id)
        movieCast.actorRemote.map { it.toActor() }
    }, error = { cause -> GetMovieCastException(cause) })
}

const val IMAGE_LIMIT = 10
