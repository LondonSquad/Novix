package com.london.data.datasource.remote.details.moviedetails

import com.london.data.datasource.remote.details.moviedetails.api.MovieDetailsApiService
import com.london.data.datasource.remote.details.moviedetails.model.moviecast.MovieCastResponse
import com.london.data.datasource.remote.details.moviedetails.model.moviedetails.MovieDetailsResponse
import com.london.data.datasource.remote.details.moviedetails.model.movieimages.MovieImagesResponse
import com.london.data.datasource.remote.details.moviedetails.model.similarmovies.SimilarMoviesResponse
import org.koin.core.annotation.Single

@Single
class MovieDetailsRemoteDataSourceImpl(
    private val movieDetailsApiService: MovieDetailsApiService,
) : MovieDetailsRemoteDataSource {
    override suspend fun getMovieDetails(movieId: Int): MovieDetailsResponse =
        movieDetailsApiService.getMovieDetails(
            movieId = movieId
        )

    override suspend fun getSimilarMovies(movieId: Int): SimilarMoviesResponse =
        movieDetailsApiService.getSimilarMovies(
            movieId = movieId
        )

    override suspend fun getMovieCast(movieId: Int): MovieCastResponse =
        movieDetailsApiService.getMovieCast(
            movieId = movieId
        )

    override suspend fun getMovieImages(movieId: Int): MovieImagesResponse =
        movieDetailsApiService.getMovieImages(
            movieId = movieId
        )
}
