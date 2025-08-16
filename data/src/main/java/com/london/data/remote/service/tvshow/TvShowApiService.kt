package com.london.data.remote.service.tvshow


import com.london.data.remote.model.ApiConstants
import com.london.data.remote.model.ApiConstants.POPULAR_TV_SHOWS_PATH
import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.details.ImagesResponse
import com.london.data.remote.model.details.actor.model.actortvshowdetails.ActorTvShowDetailsResponse
import com.london.data.remote.model.details.rating.AccountStatesResponse
import com.london.data.remote.model.details.rating.RatingRemoteBody
import com.london.data.remote.model.details.rating.RatingRemoteResponse
import com.london.data.remote.model.details.tvshow.model.TvShowDetailsRemoteResponse
import com.london.data.remote.model.details.tvshow.model.tvshowepisode.TvShowEpisodeResponse
import com.london.data.remote.model.details.tvshow.model.tvshowepisode.TvShowEpisodesRemoteResponse
import com.london.data.remote.model.details.videoprovider.VideoResponse
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

interface TvShowApiService {

    @GET(ApiConstants.TV_SHOW_DETAILS_PATH)
    suspend fun getTvShowDetails(
        @Path("tv_id") tvShowId: Int,
    ): Response<TvShowDetailsRemoteResponse>

    @GET(ApiConstants.TV_SHOW_EPISODE_BY_SEASON_PATH)
    suspend fun getTvShowEpisodesBySeason(
        @Path("tv_id") tvShowId: Int,
        @Path("season_number") seasonNumber: Int,
    ): Response<TvShowEpisodesRemoteResponse>

    @GET(ApiConstants.TV_SHOW_IMAGE)
    suspend fun getTvShowImages(
        @Path("tv_id") tvShowId: Int,
    ): Response<ImagesResponse>

    @GET(ApiConstants.EPISODE_DETAILS_PATH)
    suspend fun getEpisodeDetails(
        @Path("tv_id") tvShowId: Int,
        @Path("season_number") seasonNumber: Int,
        @Path("episode_number") episodeNumber: Int,
    ): Response<TvShowEpisodeResponse>

    @GET(ApiConstants.EPISODE_VIDEO_PATH)
    suspend fun getEpisodeVideo(
        @Path("series_id") seriesId: Int,
        @Path("season_number") seasonNumber: Int,
        @Path("episode_number") episodeNumber: Int,
    ): Response<VideoResponse>

    @GET(ApiConstants.TV_SHOW_VIDEO_PATH)
    suspend fun getTvShowVideos(
        @Path("tv_id") tvShowId: Int,
    ): Response<VideoResponse>

    @GET(ApiConstants.ACCOUNT_TV_SHOW_STATES)
    suspend fun getAccountTvShowState(
        @Path("series_id") seriesId: Int,
        @Query("guest_session_id") guestSessionId: String?,
        @Query("session_id") userSessionId: String?,
    ): Response<AccountStatesResponse>

    @GET(ApiConstants.ACCOUNT_TV_EPISODE)
    suspend fun getAccountTvEpisode(
        @Path("series_id") tvShowId: Int,
        @Path("season_number") seasonNumber: Int,
        @Path("episode_number") episodeNumber: Int,
        @Query("guest_session_id") guestSessionId: String?,
        @Query("session_id") userSessionId: String?,
    ): Response<AccountStatesResponse>

    @GET(ApiConstants.ACTOR_TV_SHOWS)
    suspend fun getActorTvShows(
        @Path("person_id") actorId: Int,
    ): Response<ActorTvShowDetailsResponse>

    @GET(ApiConstants.TV_SHOW_DISCOVER_PATH)
    suspend fun getTvShowByCategory(
        @Query("with_genres") genreId: Int,
        @Query("page") page: Int,
        @Query("include_adult") includeAdult: Boolean
    ): Response<ApiResponse<SearchTvShowRemote>>

    @GET(POPULAR_TV_SHOWS_PATH)
    suspend fun getPopularTvShows(): Response<ApiResponse<PopularTvShowResponse>>

    @GET(ApiConstants.TRENDING_TV_SHOWS_PATH)
    suspend fun getTrendingTvShows(@Query("page") page: Int): Response<ApiResponse<TrendingResponse>>

    @GET(ApiConstants.RATED_TV_SHOWS_PATH)
    suspend fun getRatedTvShows(
        @Path("account_id") accountId: Int,
        @Query("session_id") sessionId: String,
    ): Response<ApiResponse<RatingMediaResponse>>

    @POST(ApiConstants.ADD_TV_SHOW_RATING_PATH)
    suspend fun addTvShowRating(
        @Path("series_id") tvShowId: Int,
        @Query("guest_session_id") guestSessionId: String?,
        @Query("session_id") userSessionId: String?,
        @Body ratingRequest: RatingRemoteBody
    ): Response<RatingRemoteResponse>

    @POST(ApiConstants.ADD_TV_EPISODE_PATH)
    suspend fun addTvEpisode(
        @Path("series_id") tvShowId: Int,
        @Path("season_number") seasonNumber: Int,
        @Path("episode_number") episodeNumber: Int,
        @Query("guest_session_id") guestSessionId: String?,
        @Query("session_id") userSessionId: String?,
        @Body ratingRequest: RatingRemoteBody
    ): Response<RatingRemoteResponse>

    @DELETE(ApiConstants.DELETE_TV_SHOW_RATING_PATH)
    suspend fun deleteTvShowRating(
        @Path("series_id") tvShowId: Int,
        @Query("session_id") sessionId: String?
    ): Response<RatingRemoteResponse>

    @GET(ApiConstants.GET_TV_SHOW_REVIEW_PATH)
    suspend fun getTvShowReviews(
        @Path("tv_id") tvShowId: Int,
        @Query("page") page: Int
    ): Response<ApiResponse<ReviewResponse>>

    @GET(ApiConstants.GET_TOP_RATED_TV_SHOWS_PATH)
    suspend fun getTopRatedTvShows(
        @Query("page") pageNumber: Int,
    ): Response<ApiResponse<TopRatedTvSeriesRemote>>
}
