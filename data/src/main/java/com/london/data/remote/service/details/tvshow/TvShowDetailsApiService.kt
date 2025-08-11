package com.london.data.remote.service.details.tvshow


import com.london.data.remote.model.ApiConstants
import com.london.data.remote.model.ApiConstants.POPULAR_TV_SHOWS_PATH
import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.details.actor.model.actortvshowdetails.ActorTvShowDetailsResponse
import com.london.data.remote.model.details.rating.AccountStatesResponse
import com.london.data.remote.model.details.rating.RatingRemoteBody
import com.london.data.remote.model.details.rating.RatingRemoteResponse
import com.london.data.remote.model.details.tvshow.model.TvShowDetailsRemoteResponse
import com.london.data.remote.model.details.tvshow.model.TvShowImagesRemoteResponse
import com.london.data.remote.model.details.tvshow.model.tvshowepisode.EpisodeVideoResponse
import com.london.data.remote.model.details.tvshow.model.tvshowepisode.TvShowEpisodeResponse
import com.london.data.remote.model.details.tvshow.model.tvshowepisode.TvShowEpisodesRemoteResponse
import com.london.data.remote.model.details.videoprovider.tvshow.model.TvShowVideoResponse
import com.london.data.remote.model.home.popular.PopularTvShowResponse
import com.london.data.remote.model.home.toprated.TopRatedTvSeriesRemote
import com.london.data.remote.model.home.trending.TrendingResponse
import com.london.data.remote.model.myrating.RatingMediaResponse
import com.london.data.remote.model.reviews.ReviewResponse
import com.london.data.remote.model.search.SearchTvShowRemote
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface TvShowDetailsApiService {

    @GET("3/tv/{tv_id}")
    suspend fun getTvShowDetails(
        @Path("tv_id") tvShowId: Int,
    ): Response<TvShowDetailsRemoteResponse>

    @GET("3/tv/{tv_id}/season/{season_number}")
    suspend fun getTvShowEpisodesBySeason(
        @Path("tv_id") tvShowId: Int,
        @Path("season_number") seasonNumber: Int,
    ): Response<TvShowEpisodesRemoteResponse>

    @GET("3/tv/{tv_id}/images")
    suspend fun getTvShowImages(
        @Path("tv_id") tvShowId: Int,
    ): Response<TvShowImagesRemoteResponse>

    @GET("3/tv/{tv_id}/season/{season_number}/episode/{episode_number}")
    suspend fun getEpisodeDetails(
        @Path("tv_id") tvShowId: Int,
        @Path("season_number") seasonNumber: Int,
        @Path("episode_number") episodeNumber: Int,
    ): Response<TvShowEpisodeResponse>

    @GET("3/tv/{series_id}/season/{season_number}/episode/{episode_number}/videos")
    suspend fun getEpisodeVideo(
        @Path("series_id") seriesId: Int,
        @Path("season_number") seasonNumber: Int,
        @Path("episode_number") episodeNumber: Int,
    ): Response<EpisodeVideoResponse>

    @GET("3/tv/{tv_id}/videos")
    suspend fun getTvShowVideos(
        @Path("tv_id") tvShowId: Int,
    ): Response<TvShowVideoResponse>

    @GET("3/tv/{series_id}/account_states")
    suspend fun getAccountTvShowState(
        @Path("series_id") seriesId: Int,
        @Query("guest_session_id") guestSessionId: String?,
        @Query("session_id") userSessionId: String?,
    ): Response<AccountStatesResponse>

    @GET("3/tv/{series_id}/season/{season_number}/episode/{episode_number}/account_states")
    suspend fun getAccountTvEpisode(
        @Path("series_id") tvShowId: Int,
        @Path("season_number") seasonNumber: Int,
        @Path("episode_number") episodeNumber: Int,
        @Query("guest_session_id") guestSessionId: String?,
        @Query("session_id") userSessionId: String?,
    ): Response<AccountStatesResponse>

    @GET("3/person/{person_id}/tv_credits")
    suspend fun getActorTvShows(
        @Path("person_id") actorId: Int,
    ): Response<ActorTvShowDetailsResponse>

    @GET(ApiConstants.TV_SHOW_DISCOVER_PATH)
    suspend fun searchForTvShowsByCategory(
        @Query("with_genres") genreId: Int,
        @Query("page") page: Int,
        @Query("include_adult") includeAdult: Boolean
    ): Response<ApiResponse<SearchTvShowRemote>>

    @GET(POPULAR_TV_SHOWS_PATH)
    suspend fun getPopularTvShows(): Response<ApiResponse<PopularTvShowResponse>>

    @GET(ApiConstants.TRENDING_TV_SHOWS_PATH)
    suspend fun getTrendingTvShows(@Query("page") page: Int): Response<ApiResponse<TrendingResponse>>

    @GET("3/account/{account_id}/rated/tv")
    suspend fun getRatedTvShow(
        @Path("account_id") accountId: Int,
        @Query("session_id") sessionId: String,
    ): Response<ApiResponse<RatingMediaResponse>>

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

    @DELETE("/3/tv/{series_id}/rating")
    suspend fun deleteTvShowRating(
        @Path("series_id") tvShowId: Int,
        @Query("session_id") sessionId: String?
    ): Response<RatingRemoteResponse>

    @GET("3/tv/{tv_id}/reviews")
    suspend fun getTvShowReviews(
        @Path("tv_id") tvShowId: Int,
        @Query("page") page: Int
    ): Response<ApiResponse<ReviewResponse>>

    @GET("3/tv/top_rated")
    suspend fun getTopRatedTvShow(
        @Query("page") pageNumber: Int,
    ): Response<ApiResponse<TopRatedTvSeriesRemote>>
}
