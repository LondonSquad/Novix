package com.london.data.remote.source.tvshow

import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.details.actor.model.actortvshowdetails.ActorTvShowDetailsResponse
import com.london.data.remote.model.details.rating.AccountStatesResponse
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

interface TvShowRemoteDataSource {

    suspend fun getActorTvShowById(id: Int): Result<ActorTvShowDetailsResponse>
    suspend fun getPopularTvShows(): Result<ApiResponse<PopularTvShowResponse>>
    suspend fun getTrendingTvShows(page: Int): Result<ApiResponse<TrendingResponse>>
    suspend fun deleteTvShowRating(tvShowId: Int, sessionId: String?): Result<RatingRemoteResponse>
    suspend fun getTopRatedTvShows(pageNumber: Int): Result<ApiResponse<TopRatedTvSeriesRemote>>
    suspend fun getTvShowDetailsById(id: Int): Result<TvShowDetailsRemoteResponse>
    suspend fun getTvShowImagesById(id: Int): Result<TvShowImagesRemoteResponse>
    suspend fun getTvShowVideos(tvShowId: Int): Result<TvShowVideoResponse>

    suspend fun getTvShowReviews(
        tvShowId: Int,
        pageNumber: Int
    ): Result<ApiResponse<ReviewResponse>>

    suspend fun getAllRatedTvShows(
        accountId: Int,
        sessionId: String,
    ): Result<ApiResponse<RatingMediaResponse>>

    suspend fun addTvShowRating(
        tvShowId: Int,
        rating: Double,
        userSessionId: String?,
        guestSessionId: String?
    ): Result<RatingRemoteResponse>

    suspend fun addTvShowEpisode(
        tvShowId: Int,
        seasonNumber: Int,
        episodeNumber: Int,
        rating: Double,
        userSessionId: String?,
        guestSessionId: String?
    ): Result<RatingRemoteResponse>

    suspend fun getTvShowsByCategoryId(
        categoryId: Int,
        pageNumber: Int,
        includeAdult: Boolean = false
    ): Result<ApiResponse<SearchTvShowRemote>>

    suspend fun getTvShowEpisodesBySeason(
        id: Int,
        seasonNumber: Int,
    ): Result<TvShowEpisodesRemoteResponse>


    suspend fun getEpisodeDetails(
        tvShowId: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ): Result<TvShowEpisodeResponse>

    suspend fun getEpisodeVideos(
        tvShowId: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ): Result<EpisodeVideoResponse>

    suspend fun getAccountTvShowStates(
        tvShowId: Int,
        guestSessionId: String?,
        userSessionId: String?,
    ): Result<AccountStatesResponse>

    suspend fun getAccountTvEpisodeState(
        tvShowId: Int,
        seasonNumber: Int,
        episodeNumber: Int,
        guestSessionId: String?,
        userSessionId: String?,
    ): Result<AccountStatesResponse>
}