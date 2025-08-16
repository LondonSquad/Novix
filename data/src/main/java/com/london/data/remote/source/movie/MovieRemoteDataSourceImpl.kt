package com.london.data.remote.source.movie

import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.details.actor.model.actormoviedetails.ActorMovieDetailsResponse
import com.london.data.remote.model.details.movie.model.moviedetails.MovieDetailsResponse
import com.london.data.remote.model.details.movie.model.movieimages.MovieImagesResponse
import com.london.data.remote.model.details.rating.AccountStatesResponse
import com.london.data.remote.model.details.rating.RatingRemoteBody
import com.london.data.remote.model.details.rating.RatingRemoteResponse
import com.london.data.remote.model.details.videoprovider.VideoResponse
import com.london.data.remote.model.home.popular.PopularMovieResponse
import com.london.data.remote.model.home.toprated.TopRatedMovieRemote
import com.london.data.remote.model.home.trending.TrendingResponse
import com.london.data.remote.model.myrating.RatingMediaResponse
import com.london.data.remote.model.reviews.ReviewResponse
import com.london.data.remote.model.search.MovieRemote
import com.london.data.remote.service.movie.MovieApiService
import com.london.data.remote.source.base.BaseRemoteDatasource
import com.london.data.utils.getCurrentDate
import javax.inject.Inject

class MovieRemoteDataSourceImpl @Inject constructor(
    private val movieApiService: MovieApiService,
) : MovieRemoteDataSource, BaseRemoteDatasource {

    override suspend fun getMovieDetails(movieId: Int): Result<MovieDetailsResponse> {
        return callApiWithRetry(
            apiCall = { movieApiService.getMovieDetails(movieId = movieId) },
            mapper = { it })
    }

    override suspend fun getSimilarMovies(movieId: Int): Result<ApiResponse<MovieRemote>> {
        return callApiWithRetry(
            apiCall = { movieApiService.getSimilarMovies(movieId = movieId) },
            mapper = { it })
    }

    override suspend fun getMovieImages(movieId: Int): Result<MovieImagesResponse> {
        return callApiWithRetry(
            { movieApiService.getMovieImages(movieId = movieId) },
            mapper = { it })
    }

    override suspend fun getAccountMovieStates(
        movieId: Int,
        userSessionId: String?
    ): Result<AccountStatesResponse> {
        return callApiWithRetry(apiCall = {
            movieApiService.getAccountMovieStates(
                movieId = movieId,
                userSessionId = userSessionId
            )
        }, mapper = { it })
    }

    override suspend fun getMovieVideos(movieId: Int): Result<VideoResponse> {
        return callApiWithRetry(
            { movieApiService.getMovieVideos(movieId = movieId) },
            mapper = { it }
        )
    }

    override suspend fun getPopularMovies(): Result<ApiResponse<PopularMovieResponse>> {
        return callApiWithRetry(
            apiCall = { movieApiService.getPopularMovies() },
            mapper = { it }
        )
    }

    override suspend fun getTrendingMovies(page: Int): Result<ApiResponse<TrendingResponse>> {
        return callApiWithRetry(
            apiCall = { movieApiService.getTrendingMovies(page = page) },
            mapper = { it }
        )
    }

    override suspend fun getMovieReviews(
        movieId: Int, pageNumber: Int
    ): Result<ApiResponse<ReviewResponse>> {
        return callApiWithRetry(
            apiCall = { movieApiService.getMovieReviews(movieId = movieId, page = pageNumber) },
            mapper = { it }
        )
    }

    override suspend fun getTopRatedMovies(
        pageNumber: Int,
    ): Result<ApiResponse<TopRatedMovieRemote>> {
        return callApiWithRetry(
            apiCall = { movieApiService.getTopRatedMovies(pageNumber) },
            mapper = { it }
        )
    }

    override suspend fun getActorMovieById(id: Int): Result<ActorMovieDetailsResponse> {
        return callApiWithRetry(
            apiCall = { movieApiService.getActorMovies(actorId = id) },
            mapper = { it }
        )
    }

    override suspend fun deleteMovieRating(
        movieId: Int,
        sessionId: String?
    ): Result<RatingRemoteResponse> = callApiWithRetry(
        apiCall = {
            movieApiService.deleteMovieRating(
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
                movieApiService.getRatedMovies(
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
                movieApiService.addMovieRating(
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
                movieApiService.getUpComingMoviesByCategory(
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
                movieApiService.getMoviesByCategory(
                    genreId = categoryId,
                    page = pageNumber,
                    includeAdult = includeAdult
                )
            },
            mapper = { it }
        )
    }
}
