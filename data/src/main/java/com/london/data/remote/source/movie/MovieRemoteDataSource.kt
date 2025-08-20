package com.london.data.remote.source.movie

import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.account.AccountStatesResponse
import com.london.data.remote.model.details.actor.movie.ActorMovieDetailsResponse
import com.london.data.remote.model.details.image.ImagesResponse
import com.london.data.remote.model.details.movie.details.MovieDetailsResponse
import com.london.data.remote.model.details.rating.RatingRemoteResponse
import com.london.data.remote.model.details.videoprovider.VideoResponse
import com.london.data.remote.model.myrating.RatingMediaResponse
import com.london.data.remote.model.popular.PopularMovieResponse
import com.london.data.remote.model.reviews.ReviewResponse
import com.london.data.remote.model.search.SearchMovieRemote
import com.london.data.remote.model.toprated.TopRatedMovieRemote
import com.london.data.remote.model.trending.TrendingResponse

interface MovieRemoteDataSource {
    suspend fun getMovieDetails(movieId: Int): Result<MovieDetailsResponse>

    suspend fun getSimilarMovies(movieId: Int): Result<ApiResponse<SearchMovieRemote>>

    suspend fun getMovieImages(movieId: Int): Result<ImagesResponse>

    suspend fun getMovieVideos(movieId: Int): Result<VideoResponse>

    suspend fun getPopularMovies(): Result<ApiResponse<PopularMovieResponse>>

    suspend fun getTrendingMovies(page: Int): Result<ApiResponse<TrendingResponse>>

    suspend fun getMovieReviews(movieId: Int, pageNumber: Int): Result<ApiResponse<ReviewResponse>>

    suspend fun getTopRatedMovies(pageNumber: Int): Result<ApiResponse<TopRatedMovieRemote>>

    suspend fun getActorMovieById(id: Int): Result<ActorMovieDetailsResponse>

    suspend fun deleteMovieRating(movieId: Int, sessionId: String?): Result<RatingRemoteResponse>

    suspend fun addMovieRating(
        movieId: Int,
        rating: Double,
        userSessionId: String?,
        guestSessionId: String?
    ): Result<RatingRemoteResponse>

    suspend fun getAllRatedMovies(
        accountId: Int,
        sessionId: String,
    ): Result<ApiResponse<RatingMediaResponse>>

    suspend fun getUpComingMoviesByCategory(
        categoryId: Int? = null,
        pageNumber: Int,
        includeAdult: Boolean = false
    ): Result<ApiResponse<SearchMovieRemote>>

    suspend fun getMoviesByCategory(
        categoryId: Int, pageNumber: Int, includeAdult: Boolean = false
    ): Result<ApiResponse<SearchMovieRemote>>

    suspend fun getAccountMovieStates(
        movieId: Int,
        userSessionId: String?
    ): Result<AccountStatesResponse>

}
