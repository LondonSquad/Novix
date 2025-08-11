package com.london.data.remote.service.myrating

import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.details.rating.RatingRemoteBody
import com.london.data.remote.model.details.rating.RatingRemoteResponse
import com.london.data.remote.model.myrating.RatingMediaResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface RatingApiService {
    @GET("3/account/{account_id}/rated/movies")
    suspend fun getRatedMovies(
        @Path("account_id") accountId: Int,
        @Query("session_id") sessionId: String,
    ): Response<ApiResponse<RatingMediaResponse>>

    @GET("3/account/{account_id}/rated/tv")
    suspend fun getRatedTvShow(
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

    @POST("3/tv/{series_id}/rating")
    suspend fun addTvShowRating(
        @Path("series_id") tvShowId: Int,
        @Query("guest_session_id") guestSessionId: String?,
        @Query("session_id") userSessionId: String?,
        @Body ratingRequest: RatingRemoteBody
    ): Response<RatingRemoteResponse>

    @POST("3/tv/{series_id}/season/{season_number}/episode/{episode_number}/rating")
    suspend fun addTvEpisode(
        @Path("series_id") tvShowId: Int,
        @Path("season_number") seasonNumber: Int,
        @Path("episode_number") episodeNumber: Int,
        @Query("guest_session_id") guestSessionId: String?,
        @Query("session_id") userSessionId: String?,
        @Body ratingRequest: RatingRemoteBody
    ): Response<RatingRemoteResponse>

    @DELETE("3/movie/{movie_id}/rating")
    suspend fun deleteMovieRating(
        @Path("movie_id") movieId: Int,
        @Query("session_id") sessionId: String?
    ): Response<RatingRemoteResponse>

    @DELETE("/3/tv/{series_id}/rating")
    suspend fun deleteTvShowRating(
        @Path("series_id") tvShowId: Int,
        @Query("session_id") sessionId: String?
    ): Response<RatingRemoteResponse>
}
