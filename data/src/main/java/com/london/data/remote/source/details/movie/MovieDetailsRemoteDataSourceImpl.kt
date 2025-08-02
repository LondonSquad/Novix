package com.london.data.remote.source.details.movie

import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.details.movie.model.moviecast.MovieCastResponse
import com.london.data.remote.model.details.movie.model.moviedetails.MovieDetailsResponse
import com.london.data.remote.model.details.movie.model.moviedetails.MovieStatesResponse
import com.london.data.remote.model.details.movie.model.movieimages.MovieImagesResponse
import com.london.data.remote.model.search.model.SearchMovieRemote
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

    override suspend fun getSimilarMovies(movieId: Int): Result<ApiResponse<SearchMovieRemote>> =
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

    override suspend fun getMovieStates(
        movieId: Int,
        guestSessionId: String?,
        userSessionId: String?
    ): Result<MovieStatesResponse> =
        callApiWithRetry(apiCall = {
            movieDetailsApiService.getMovieStates(
                movieId = movieId,
                guestSessionId = guestSessionId,
                userSessionId = userSessionId
            )
        }, mapper = { it })

}
