package com.london.data.remote.source.details.movie

import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.details.movie.model.moviecast.MovieCastResponse
import com.london.data.remote.model.details.movie.model.moviedetails.MovieDetailsResponse
import com.london.data.remote.model.details.movie.model.movieimages.MovieImagesResponse
import com.london.data.remote.model.details.rating.AccountStatesResponse
import com.london.data.remote.model.details.videoprovider.movie.model.MovieVideoRemote
import com.london.data.remote.model.list.CustomMovieListResponse
import com.london.data.remote.model.search.MovieRemote
import com.london.data.remote.service.details.movie.MovieDetailsApiService
import com.london.data.remote.source.base.BaseRemoteDatasource
import javax.inject.Inject

class MovieDetailsRemoteDataSourceImpl @Inject constructor(
    private val movieDetailsApiService: MovieDetailsApiService,
) : MovieDetailsRemoteDataSource, BaseRemoteDatasource {
    override suspend fun getMovieDetails(movieId: Int): Result<MovieDetailsResponse> =
        callApiWithRetry(
            apiCall = { movieDetailsApiService.getMovieDetails(movieId = movieId) },
            mapper = { it })

    override suspend fun getSimilarMovies(movieId: Int): Result<ApiResponse<MovieRemote>> =
        callApiWithRetry(
            apiCall = { movieDetailsApiService.getSimilarMovies(movieId = movieId) },
            mapper = { it })

    override suspend fun getMovieCast(movieId: Int): Result<MovieCastResponse> = callApiWithRetry(
        apiCall = { movieDetailsApiService.getMovieCast(movieId = movieId) },
        mapper = { it })

    override suspend fun getMovieImages(movieId: Int): Result<MovieImagesResponse> =
        callApiWithRetry(
            { movieDetailsApiService.getMovieImages(movieId = movieId) },
            mapper = { it })

    override suspend fun getAccountMovieStates(
        movieId: Int,
        userSessionId: String?
    ): Result<AccountStatesResponse> =
        callApiWithRetry(apiCall = {
            movieDetailsApiService.getAccountMovieStates(
                movieId = movieId,
                userSessionId = userSessionId
            )
        }, mapper = { it })

    override suspend fun getMovieVideos(movieId: Int): Result<MovieVideoRemote> =
        callApiWithRetry(
            { movieDetailsApiService.getMovieVideos(movieId = movieId) },
            mapper = { it }
        )

    override suspend fun getMovieLists(movieId: Int): Result<ApiResponse<CustomMovieListResponse>> {
        return callApiWithRetry(
            apiCall = { movieDetailsApiService.getMovieLists(movieId = movieId) },
            mapper = { it }
        )
    }
}
