package com.london.data.repository

import com.london.data.mapper.moviedetails.toEntity
import com.london.data.mapper.moviedetails.toGenre
import com.london.data.remote.source.details.movie.MovieDetailsRemoteDataSource
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.isTrue
import com.london.domain.entity.Actor
import com.london.domain.entity.moviedatails.MovieDetails
import com.london.domain.entity.moviedatails.SimilarMovie
import com.london.domain.repository.MovieDetailsRepository
import org.koin.core.annotation.Single

@Single
class MovieDetailsRepositoryImpl(
    private val movieDetailsRemoteDataSource: MovieDetailsRemoteDataSource,
) : MovieDetailsRepository {

    override suspend fun getMovieById(id: Int): MovieDetails {
        val remoteDetails = movieDetailsRemoteDataSource.getMovieDetails(id)
        return remoteDetails.getOrThrow().toEntity()
    }

    override suspend fun getSimilarMoviesById(id: Int): List<SimilarMovie> =
        movieDetailsRemoteDataSource.getSimilarMovies(id).getOrThrow().similarMovieRemotes.orEmpty()
            .map { it.toEntity() }

    override suspend fun getMovieImagesById(id: Int): List<String> {
        val images = movieDetailsRemoteDataSource.getMovieImages(id).getOrThrow()
        return when {
            images.backdrops.orEmpty().isNotEmpty().isTrue -> images.backdrops.orEmpty()
                .map { it.filePath.asImageUrlOrEmpty() }

            images.posters.orEmpty().isNotEmpty().isTrue -> images.posters.orEmpty()
                .map { it.filePath.asImageUrlOrEmpty() }

            images.logos.orEmpty().isNotEmpty().isTrue -> images.logos.orEmpty()
                .map { it.filePath.asImageUrlOrEmpty() }

            else -> emptyList()
        }.take(IMAGE_LIMIT)
    }

    override suspend fun getMovieCastById(id: Int): List<Actor> {
        val movieCast = movieDetailsRemoteDataSource.getMovieCast(id).getOrThrow()
        return movieCast.actorRemote?.map { it.toEntity() }.orEmpty()
    }


    companion object {
        private const val IMAGE_LIMIT = 10
    }
}
