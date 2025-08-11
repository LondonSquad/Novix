package com.london.data.remote.service.movie


import com.london.data.remote.model.ApiConstants
import com.london.data.remote.model.ApiConstants.POPULAR_MOVIES_PATH
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
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {

    @GET("3/movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
    ): Response<MovieDetailsResponse>

    @GET("3/movie/{movie_id}/similar")
    suspend fun getSimilarMovies(
        @Path("movie_id") movieId: Int,
    ): Response<ApiResponse<MovieRemote>>

    @GET("3/movie/{movie_id}/images")
    suspend fun getMovieImages(
        @Path("movie_id") movieId: Int,
    ): Response<MovieImagesResponse>

    @GET("3/movie/{movie_id}/videos")
    suspend fun getMovieVideos(
        @Path("movie_id") movieId: Int,
    ): Response<MovieVideoRemote>

    @GET("3/movie/{movie_id}/account_states")
    suspend fun getAccountMovieStates(
        @Path("movie_id") movieId: Int,
        @Query("session_id") userSessionId: String?,
    ): Response<AccountStatesResponse>

    @GET("3/person/{person_id}/movie_credits")
    suspend fun getActorMovies(
        @Path("person_id") actorId: Int,
    ): Response<ActorMovieDetailsResponse>

    @GET(ApiConstants.MOVIE_DISCOVER_PATH)
    suspend fun getMoviesByCategory(
        @Query("with_genres") genreId: Int,
        @Query("page") page: Int,
        @Query("include_adult") includeAdult: Boolean
    ): Response<ApiResponse<MovieRemote>>

    @GET(POPULAR_MOVIES_PATH)
    suspend fun getPopularMovies(): Response<ApiResponse<PopularMovieResponse>>

    @GET(ApiConstants.TRENDING_MOVIES_PATH)
    suspend fun getTrendingMovies(@Query("page") page: Int): Response<ApiResponse<TrendingResponse>>

    @GET(ApiConstants.MOVIE_DISCOVER_PATH)
    suspend fun getUpComingMoviesByCategory(
        @Query("with_genres") genreId: Int?=null,
        @Query("primary_release_date.gte") releaseDate: String,
        @Query("sort_by") sortBy: String = "primary_release_date.asc",
        @Query("page") page: Int,
        @Query("include_adult") includeAdult: Boolean
    ): Response<ApiResponse<MovieRemote>>

    @GET("3/account/{account_id}/rated/movies")
    suspend fun getRatedMovies(
        @Path("account_id") accountId: Int,
        @Query("session_id") sessionId: String,
    ): Response<ApiResponse<RatingMediaResponse>>

    @POST("3/movie/{movie_id}/rating")
    suspend fun addMovieRating(
        @Path("movie_id") movieId: Int,
        @Query("guest_session_id") guestSessionId: String?,
        @Query("session_id") userSessionId: String?,
        @Body ratingRequest: RatingRemoteBody
    ): Response<RatingRemoteResponse>

    @DELETE("3/movie/{movie_id}/rating")
    suspend fun deleteMovieRating(
        @Path("movie_id") movieId: Int,
        @Query("session_id") sessionId: String?
    ): Response<RatingRemoteResponse>

    @GET("3/movie/{movie_id}/reviews")
    suspend fun getMovieReviews(
        @Path("movie_id") movieId: Int,
        @Query("page") page: Int
    ): Response<ApiResponse<ReviewResponse>>

    @GET("3/movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("page") pageNumber: Int,
    ): Response<ApiResponse<TopRatedMovieRemote>>
}