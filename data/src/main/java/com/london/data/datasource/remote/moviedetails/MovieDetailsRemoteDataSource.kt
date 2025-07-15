package com.london.data.datasource.remote.moviedetails

import com.london.data.datasource.remote.moviedetails.model.MovieCastRemote
import com.london.data.datasource.remote.moviedetails.model.MovieDetailsRemote
import com.london.data.datasource.remote.moviedetails.model.MovieImages
import com.london.data.datasource.remote.moviedetails.model.SimilarMovies

interface MovieDetailsRemoteDataSource {
    suspend fun getMovieDetails(movieId: Int): MovieDetailsRemote
    suspend fun getSimilarMovies(movieId: Int): SimilarMovies
    suspend fun getMovieCast(movieId: Int): MovieCastRemote
    suspend fun getMovieImages(movieId: Int): MovieImages
}
