package com.london.data.datasource.remote.details.videoprovider.movie

import com.london.data.datasource.remote.BaseRemoteDatasource
import com.london.data.datasource.remote.details.moviedetails.api.MovieDetailsApiService
import com.london.data.datasource.remote.details.videoprovider.movie.model.MovieVideoRemote
import org.koin.core.annotation.Single

@Single
class MovieVideoProviderRemoteImpl(
    private val movieDetailsApiService: MovieDetailsApiService,
) : MovieVideoProviderRemote, BaseRemoteDatasource {
    override suspend fun getMovieVideos(movieId: Int): Result<MovieVideoRemote> =
        callApiWithRetry(
            { movieDetailsApiService.getMovieVideos(movieId = movieId) },
            mapper = { it }
        )
}
