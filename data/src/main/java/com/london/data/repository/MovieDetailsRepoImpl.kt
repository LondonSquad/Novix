package com.london.data.repository

import com.london.data.datasource.remote.details.moviedetails.MovieDetailsRemoteDataSource
import com.london.data.mapper.moviedetails.toEntity
import com.london.data.mapper.moviedetails.toGenre
import com.london.data.mapper.moviedetails.toSimilarMovie
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.isTrue
import com.london.domain.entity.Actor
import com.london.domain.entity.moviedatails.MovieDetails
import com.london.domain.entity.moviedatails.SimilarMovie
import com.london.domain.repository.MovieDetailsRepository
import org.koin.core.annotation.Single

@Single
class MovieDetailsRepoImpl(
    private val movieDetailsRemoteDataSource: MovieDetailsRemoteDataSource,
) : MovieDetailsRepository {

    override suspend fun getMovieById(id: Int): MovieDetails {
        val remoteDetails = movieDetailsRemoteDataSource.getMovieDetails(id)
        return remoteDetails.getOrThrow().toEntity(
            genres = remoteDetails.getOrThrow().genreRemote?.map { it.toGenre() }.orEmpty(),
            movieImages = getMovieImagesById(id),
            movieDuration = remoteDetails.getOrThrow().runtime?.toString().orEmpty()
        )
    }

    override suspend fun getSimilarMoviesById(id: Int): List<SimilarMovie> {
        val similarMoviesRemote = movieDetailsRemoteDataSource.getSimilarMovies(id)
        return similarMoviesRemote.getOrThrow().similarMovieRemotes?.map { it.toSimilarMovie() }
            .orEmpty()
    }

    override suspend fun getMovieImagesById(id: Int): List<String> {
        val images = movieDetailsRemoteDataSource.getMovieImages(id).getOrThrow()
        return when {
            images.backdrops?.isNotEmpty().isTrue -> images.backdrops?.map { it.filePath.asImageUrlOrEmpty() }
            images.posters?.isNotEmpty().isTrue -> images.posters?.map { it.filePath.asImageUrlOrEmpty() }
            images.logos?.isNotEmpty().isTrue -> images.logos?.map { it.filePath.asImageUrlOrEmpty() }
            else -> emptyList()
        }?.take(IMAGE_LIMIT).orEmpty()
    }

    override suspend fun getMovieCastById(id: Int): List<Actor> {
        val movieCast = movieDetailsRemoteDataSource.getMovieCast(id).getOrThrow()
        return movieCast.actorRemote?.map { it.toEntity() }.orEmpty()
    }

    companion object {
        private const val IMAGE_LIMIT = 10
    }
}
