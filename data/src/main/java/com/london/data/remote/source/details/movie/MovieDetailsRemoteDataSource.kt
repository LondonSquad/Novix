package com.london.data.remote.source.details.movie

import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.details.movie.model.moviecast.MovieCastResponse
import com.london.data.remote.model.details.movie.model.moviedetails.MovieAccountStatesResponse
import com.london.data.remote.model.details.movie.model.moviedetails.AccountMovieStatesResponse
import com.london.data.remote.model.details.movie.model.moviedetails.MovieDetailsResponse
import com.london.data.remote.model.details.movie.model.movieimages.MovieImagesResponse
import com.london.data.remote.model.search.model.SearchMovieRemote


interface MovieDetailsRemoteDataSource {
    suspend fun getMovieDetails(movieId: Int): Result<MovieDetailsResponse>
    suspend fun getSimilarMovies(movieId: Int): Result<ApiResponse<SearchMovieRemote>>
    suspend fun getMovieCast(movieId: Int): Result<MovieCastResponse>
    suspend fun getMovieImages(movieId: Int): Result<MovieImagesResponse>
    suspend fun getAccountMovieStates(
        movieId: Int,
        guestSessionId: String?,
        userSessionId: String?
    ) : Result<AccountMovieStatesResponse>
}
