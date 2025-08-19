package com.london.data.remote.service.movie


import com.london.data.remote.model.ApiConstants
import com.london.data.remote.model.ApiConstants.POPULAR_MOVIES_PATH
import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.account.AccountStatesResponse
import com.london.data.remote.model.details.actor.movie.ActorMovieDetailsResponse
import com.london.data.remote.model.details.image.ImagesResponse
import com.london.data.remote.model.details.movie.details.MovieDetailsResponse
import com.london.data.remote.model.details.rating.RatingRemoteBody
import com.london.data.remote.model.details.rating.RatingRemoteResponse
import com.london.data.remote.model.details.videoprovider.VideoResponse
import com.london.data.remote.model.myrating.RatingMediaResponse
import com.london.data.remote.model.popular.PopularMovieResponse
import com.london.data.remote.model.reviews.ReviewResponse
import com.london.data.remote.model.search.SearchMovieRemote
import com.london.data.remote.model.toprated.TopRatedMovieRemote
import com.london.data.remote.model.trending.TrendingResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiService {

    @GET(ApiConstants.MOVIE_DETAILS_PATH)
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
    ): Response<MovieDetailsResponse>

    @GET(ApiConstants.SIMILAR_MOVIES_PATH)
    suspend fun getSimilarMovies(
        @Path("movie_id") movieId: Int,
    ): Response<ApiResponse<SearchMovieRemote>>

    @GET(ApiConstants.MOVIE_IMAGES_PATH)
    suspend fun getMovieImages(
        @Path("movie_id") movieId: Int,
    ): Response<ImagesResponse>

    @GET(ApiConstants.MOVIE_VIDEOS_PATH)
    suspend fun getMovieVideos(
        @Path("movie_id") movieId: Int,
    ): Response<VideoResponse>

    @GET(ApiConstants.ACCOUNT_MOVIE_STATES)
    suspend fun getAccountMovieStates(
        @Path("movie_id") movieId: Int,
        @Query("session_id") userSessionId: String?,
    ): Response<AccountStatesResponse>

    @GET(ApiConstants.ACTOR_MOVIES_PATH)
    suspend fun getActorMovies(
        @Path("person_id") actorId: Int,
    ): Response<ActorMovieDetailsResponse>

    @GET(ApiConstants.MOVIE_DISCOVER_PATH)
    suspend fun getMoviesByCategory(
        @Query("with_genres") genreId: Int,
        @Query("page") page: Int,
        @Query("include_adult") includeAdult: Boolean
    ): Response<ApiResponse<SearchMovieRemote>>

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
    ): Response<ApiResponse<SearchMovieRemote>>

    @GET(ApiConstants.RATED_MOVIES_PATH)
    suspend fun getRatedMovies(
        @Path("account_id") accountId: Int,
        @Query("session_id") sessionId: String,
    ): Response<ApiResponse<RatingMediaResponse>>

    @POST(ApiConstants.ADD_MOVIE_RATING_PATH)
    suspend fun addMovieRating(
        @Path("movie_id") movieId: Int,
        @Query("guest_session_id") guestSessionId: String?,
        @Query("session_id") userSessionId: String?,
        @Body ratingRequest: RatingRemoteBody
    ): Response<RatingRemoteResponse>

    @DELETE(ApiConstants.DELETE_MOVIE_RATING_PATH)
    suspend fun deleteMovieRating(
        @Path("movie_id") movieId: Int,
        @Query("session_id") sessionId: String?
    ): Response<RatingRemoteResponse>

    @GET(ApiConstants.MOVIE_REVIEW_PATH)
    suspend fun getMovieReviews(
        @Path("movie_id") movieId: Int,
        @Query("page") page: Int
    ): Response<ApiResponse<ReviewResponse>>

    @GET(ApiConstants.TOP_RATED_MOVIES_PATH)
    suspend fun getTopRatedMovies(
        @Query("page") pageNumber: Int,
    ): Response<ApiResponse<TopRatedMovieRemote>>
}
