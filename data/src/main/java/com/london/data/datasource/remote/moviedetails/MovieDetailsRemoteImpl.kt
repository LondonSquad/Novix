package com.london.data.datasource.remote.moviedetails

import com.london.data.datasource.remote.moviedetails.model.MovieCastRemote
import com.london.data.datasource.remote.moviedetails.model.MovieDetailsRemote
import com.london.data.datasource.remote.moviedetails.model.MovieImages
import com.london.data.datasource.remote.moviedetails.model.SimilarMovies
import io.ktor.client.HttpClient

class MovieDetailsRemoteImpl(
    private val ktorClient: HttpClient,
) : MovieDetailsRemoteDataSource {

    override suspend fun getMovieDetails(movieId: Int): MovieDetailsRemote = runOrThrowHandler(block = {
        fetchData(
            path = "${ApiConstantsMovieDetails.MOVIE_DETAILS_PATH}/$movieId",
            tag = "getMovieDetails",
            ktorClient = ktorClient
        )
    }, error = { cause -> Exception(cause) })

    override suspend fun getSimilarMovies(movieId: Int): SimilarMovies = runOrThrowHandler(block = {
        fetchData(
            path = "${ApiConstantsMovieDetails.MOVIE_DETAILS_PATH}/$movieId/similar",
            tag = "getSimilarMovieRemotes",
            ktorClient = ktorClient
        )
    }, error = { cause -> Exception(cause) })

    override suspend fun getMovieCast(movieId: Int): MovieCastRemote = runOrThrowHandler(block = {
        fetchData(
            path = "${ApiConstantsMovieDetails.MOVIE_DETAILS_PATH}/$movieId/credits",
            tag = "getActorDetails",
            ktorClient = ktorClient
        )
    }, error = { cause -> Exception(cause) })

    override suspend fun getMovieImages(movieId: Int): MovieImages = runOrThrowHandler(block = {
        fetchData(
            path = "${ApiConstantsMovieDetails.MOVIE_DETAILS_PATH}/$movieId/images",
            tag = "getMovieImages",
            ktorClient = ktorClient
        )
    }, error = { cause -> Exception(cause) })
}
