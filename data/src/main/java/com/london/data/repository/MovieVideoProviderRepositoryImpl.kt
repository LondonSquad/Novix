package com.london.data.repository

import com.london.data.datasource.remote.details.moviedetails.GetMovieVideosFailedException
import com.london.data.datasource.remote.details.moviedetails.runOrThrow
import com.london.data.datasource.remote.details.videoprovider.movie.MovieVideoProviderRemote
import com.london.data.mapper.videoprovider.movie.toMovie
import com.london.domain.entity.videoprovider.MovieVideo
import com.london.domain.repository.MovieVideoProviderRepository
import org.koin.core.annotation.Single

@Single
class MovieVideoProviderRepositoryImpl(
    private val movieVideoProviderRemote: MovieVideoProviderRemote,
) : MovieVideoProviderRepository {
    override suspend fun getMovieVideos(movieId: Int): List<MovieVideo> =
        runOrThrow(
            block = {
                movieVideoProviderRemote.getMovieVideos(movieId).movies?.map { movieVideoRemote ->
                    movieVideoRemote.toMovie()
                }.orEmpty()
            },
            error = { cause -> GetMovieVideosFailedException(cause) }
        )
}
