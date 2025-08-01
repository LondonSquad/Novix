package com.london.data.remote.source.details.videoprovider.movie

import com.london.data.remote.model.details.videoprovider.movie.model.MovieVideoRemote
import com.london.data.remote.service.details.movie.MovieDetailsApiService
import com.london.data.remote.source.base.BaseRemoteDatasource
import javax.inject.Inject

class MovieVideoProviderRemoteImpl @Inject constructor(
    private val movieDetailsApiService: MovieDetailsApiService,
) : MovieVideoProviderRemote, BaseRemoteDatasource {
    override suspend fun getMovieVideos(movieId: Int): Result<MovieVideoRemote> =
        callApiWithRetry(
            { movieDetailsApiService.getMovieVideos(movieId = movieId) },
            mapper = { it }
        )
}
