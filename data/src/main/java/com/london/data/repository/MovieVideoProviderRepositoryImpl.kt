package com.london.data.repository

import com.london.data.mapper.videoprovider.movie.toMovie
import com.london.data.remote.source.details.videoprovider.movie.MovieVideoProviderRemote
import com.london.domain.entity.videoprovider.MovieVideo
import com.london.domain.repository.MovieVideoProviderRepository
import org.koin.core.annotation.Single

@Single
class MovieVideoProviderRepositoryImpl(
    private val movieVideoProviderRemote: MovieVideoProviderRemote,
) : MovieVideoProviderRepository {
    override suspend fun getMovieVideos(movieId: Int): List<MovieVideo> =
        movieVideoProviderRemote.getMovieVideos(movieId)
            .getOrThrow().movies?.map { movieVideoRemote ->
            movieVideoRemote.toMovie()
        }.orEmpty()
}
