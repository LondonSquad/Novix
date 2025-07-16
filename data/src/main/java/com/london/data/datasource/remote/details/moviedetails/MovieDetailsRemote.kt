package com.london.data.datasource.remote.details.moviedetails

import com.london.data.datasource.remote.details.moviedetails.model.MovieDetailsResponse
import com.london.data.datasource.remote.details.moviedetails.model.moviecast.MovieCastResponse
import com.london.data.datasource.remote.details.moviedetails.model.movieimages.MovieImagesResponse
import com.london.data.datasource.remote.details.moviedetails.model.similarmovies.SimilarMoviesResponse

interface MovieDetailsRemote {
    suspend fun getMovieDetails(movieId: Int): MovieDetailsResponse
    suspend fun getSimilarMovies(movieId: Int): SimilarMoviesResponse
    suspend fun getMovieCast(movieId: Int): MovieCastResponse
    suspend fun getMovieImages(movieId: Int): MovieImagesResponse
}
