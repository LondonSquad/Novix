package com.london.data.remote.source.movie

import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.details.actor.model.actormoviedetails.ActorMovieDetailsResponse
import com.london.data.remote.model.details.movie.model.moviedetails.MovieDetailsResponse
import com.london.data.remote.model.details.movie.model.movieimages.MovieImagesResponse
import com.london.data.remote.model.details.rating.AccountStatesResponse
import com.london.data.remote.model.details.rating.RatingRemoteBody
import com.london.data.remote.model.details.rating.RatingRemoteResponse
import com.london.data.remote.model.details.videoprovider.movie.model.MovieVideoRemote
import com.london.data.remote.model.home.popular.PopularMovieResponse
import com.london.data.remote.model.home.toprated.TopRatedMovieRemote
import com.london.data.remote.model.home.trending.TrendingResponse
import com.london.data.remote.model.myrating.RatingMediaResponse
import com.london.data.remote.model.reviews.ReviewResponse
import com.london.data.remote.model.search.MovieRemote
import com.london.data.remote.service.movie.MovieService
import com.london.data.remote.source.base.BaseRemoteDatasource
import com.london.data.utils.getCurrentDate
import javax.inject.Inject

class MovieRemoteDataSourceImpl @Inject constructor(
    private val movieService: MovieService,
) : MovieRemoteDataSource, BaseRemoteDatasource {

    override suspend fun getMovieDetails(movieId: Int): Result<MovieDetailsResponse> {
        return callApiWithRetry(
            apiCall = { movieService.getMovieDetails(movieId = movieId) },
            mapper = { it })
    }

    override suspend fun getSimilarMovies(movieId: Int): Result<ApiResponse<MovieRemote>> {
        return callApiWithRetry(
            apiCall = { movieService.getSimilarMovies(movieId = movieId) },
            mapper = { it })
    }

    override suspend fun getMovieImages(movieId: Int): Result<MovieImagesResponse> {
        return callApiWithRetry(
            { movieService.getMovieImages(movieId = movieId) },
            mapper = { it })
    }

    override suspend fun getAccountMovieStates(
        movieId: Int,
        userSessionId: String?
    ): Result<AccountStatesResponse> {
        return callApiWithRetry(apiCall = {
            movieService.getAccountMovieStates(
                movieId = movieId,
                userSessionId = userSessionId
            )
        }, mapper = { it })
    }

    override suspend fun getMovieVideos(movieId: Int): Result<MovieVideoRemote> {
        return callApiWithRetry(
            { movieService.getMovieVideos(movieId = movieId) },
            mapper = { it }
        )
    }

    override suspend fun getPopularMovies(): Result<ApiResponse<PopularMovieResponse>> {
        return callApiWithRetry(
            apiCall = { movieService.getPopularMovies() },
            mapper = { it }
        )
    }

    override suspend fun getTrendingMovies(page: Int): Result<ApiResponse<TrendingResponse>> {
        return callApiWithRetry(
            apiCall = { movieService.getTrendingMovies(page = page) },
            mapper = { it }
        )
    }

    override suspend fun getMovieReviews(
        movieId: Int, pageNumber: Int
    ): Result<ApiResponse<ReviewResponse>> {
        return callApiWithRetry(
            apiCall = { movieService.getMovieReviews(movieId = movieId, page = pageNumber) },
            mapper = { it }
        )
    }

    override suspend fun getTopRatedMovies(
        pageNumber: Int,
    ): Result<ApiResponse<TopRatedMovieRemote>> {
        return callApiWithRetry(
            apiCall = { movieService.getTopRatedMovies(pageNumber) },
            mapper = { it }
        )
    }

    override suspend fun getActorMovieById(id: Int): Result<ActorMovieDetailsResponse> {
        return callApiWithRetry(
            apiCall = { movieService.getActorMovies(actorId = id) },
            mapper = { it }
        )
    }

    override suspend fun deleteMovieRating(
        movieId: Int,
        sessionId: String?
    ): Result<RatingRemoteResponse> = callApiWithRetry(
        apiCall = {
            movieService.deleteMovieRating(
                movieId = movieId,
                sessionId = sessionId
            )
        },
        mapper = { it }
    )

    override suspend fun getAllRatedMovies(
        accountId: Int,
        sessionId: String,
    ): Result<ApiResponse<RatingMediaResponse>> {
        return callApiWithRetry(
            apiCall = {
                movieService.getRatedMovies(
                    accountId = accountId,
                    sessionId = sessionId,
                )
            },
            mapper = { it }
        )
    }

    override suspend fun addMovieRating(
        movieId: Int,
        rating: Double,
        userSessionId: String?,
        guestSessionId: String?
    ): Result<RatingRemoteResponse> {
        return callApiWithRetry(
            apiCall = {
                movieService.addMovieRating(
                    movieId = movieId,
                    guestSessionId = guestSessionId,
                    userSessionId = userSessionId,
                    ratingRequest = RatingRemoteBody(value = rating.toInt())
                )
            },
            mapper = { it }
        )
    }

    override suspend fun getUpComingMoviesByCategory(
        categoryId: Int?,
        pageNumber: Int,
        includeAdult: Boolean
    ): Result<ApiResponse<MovieRemote>> {
        return callApiWithRetry(
            {
                movieService.getUpComingMoviesByCategory(
                    genreId = categoryId,
                    releaseDate = getCurrentDate(),
                    page = pageNumber,
                    includeAdult = includeAdult
                )
            },
            mapper = { it }
        )
    }

    override suspend fun getMoviesByCategory(
        categoryId: Int,
        pageNumber: Int,
        includeAdult: Boolean
    ): Result<ApiResponse<MovieRemote>> {
        return callApiWithRetry(
            apiCall = {
                movieService.getMoviesByCategory(
                    genreId = categoryId,
                    page = pageNumber,
                    includeAdult = includeAdult
                )
            },
            mapper = { it }
        )
    }
}
