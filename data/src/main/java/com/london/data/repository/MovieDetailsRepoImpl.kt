package com.london.data.repository

import com.london.data.datasource.remote.details.moviedetails.MovieDetailsRemote
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

private const val IMAGE_LIMIT = 10

@Single
class MovieDetailsRepoImpl(
    private val movieDetailsRemote: MovieDetailsRemote,
) : MovieDetailsRepository {

    override suspend fun getMovieById(id: Int): MovieDetails {
        val remoteDetails = movieDetailsRemote.getMovieDetails(id)
        return remoteDetails.toEntity(
            genres = remoteDetails.genreRemote?.map { it.toGenre() }.orEmpty(),
            movieImages = getMovieImagesById(id),
            movieDuration = remoteDetails.runtime?.toString().orEmpty()
        )
    }

    override suspend fun getSimilarMoviesById(id: Int): List<SimilarMovie> {
        val similarMoviesRemote = movieDetailsRemote.getSimilarMovies(id)
        return similarMoviesRemote.similarMovieRemotes?.map { it.toSimilarMovie() }.orEmpty()
    }

    override suspend fun getMovieImagesById(id: Int): List<String> {
        val images = movieDetailsRemote.getMovieImages(id)
        return when {
            images.backdrops?.isNotEmpty().isTrue -> images.backdrops?.map { it.filePath.asImageUrlOrEmpty() }
            images.posters?.isNotEmpty().isTrue -> images.posters?.map { it.filePath.asImageUrlOrEmpty() }
            images.logos?.isNotEmpty().isTrue -> images.logos?.map { it.filePath.asImageUrlOrEmpty() }
            else -> emptyList()
        }?.take(IMAGE_LIMIT).orEmpty()
    }

    override suspend fun getMovieCastById(id: Int): List<Actor> {
        val movieCast = movieDetailsRemote.getMovieCast(id)
        return movieCast.actorRemote?.map { it.toEntity() } ?: emptyList()
    }
}
