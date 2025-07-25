package com.london.data.remote.source.details.movie

import com.london.data.datasource.remote.BaseRemoteDatasource
import com.london.data.datasource.remote.details.moviedetails.api.MovieDetailsApiService
import com.london.data.remote.model.details.movie.model.moviecast.MovieCastResponse
import com.london.data.remote.model.details.movie.model.moviedetails.MovieDetailsResponse
import com.london.data.remote.model.details.movie.model.movieimages.MovieImagesResponse
import com.london.data.remote.model.details.movie.model.similarmovies.SimilarMoviesResponse
import org.koin.core.annotation.Single

@Single
class MovieDetailsRemoteDataSourceImpl(
    private val movieDetailsApiService: MovieDetailsApiService,
) : MovieDetailsRemoteDataSource, BaseRemoteDatasource {
    override suspend fun getMovieDetails(movieId: Int): Result<MovieDetailsResponse> =
        callApiWithRetry(
            apiCall = { movieDetailsApiService.getMovieDetails(movieId = movieId) },
            mapper = { it }
        )

    override suspend fun getSimilarMovies(movieId: Int): Result<SimilarMoviesResponse> =
        callApiWithRetry(
            apiCall = { movieDetailsApiService.getSimilarMovies(movieId = movieId) },
            mapper = { it }
        )

    override suspend fun getMovieCast(movieId: Int): Result<MovieCastResponse> =
        callApiWithRetry(
            apiCall = { movieDetailsApiService.getMovieCast(movieId = movieId) },
            mapper = { it }
        )

    override suspend fun getMovieImages(movieId: Int): Result<MovieImagesResponse> =
        callApiWithRetry(
            { movieDetailsApiService.getMovieImages(movieId = movieId) },
            mapper = { it }
        )
}
