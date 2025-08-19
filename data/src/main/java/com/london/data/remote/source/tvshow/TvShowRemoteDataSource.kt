package com.london.data.remote.source.tvshow

import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.account.AccountStatesResponse
import com.london.data.remote.model.details.ImagesResponse
import com.london.data.remote.model.details.actor.tvshow.ActorTvShowDetailsResponse
import com.london.data.remote.model.details.rating.RatingRemoteResponse
import com.london.data.remote.model.details.tvshow.TvShowDetailsRemoteResponse
import com.london.data.remote.model.details.tvshow.episode.EpisodeDetailsResponse
import com.london.data.remote.model.details.tvshow.episode.SeasonEpisodesResponse
import com.london.data.remote.model.details.videoprovider.VideoResponse
import com.london.data.remote.model.myrating.RatingMediaResponse
import com.london.data.remote.model.popular.PopularTvShowResponse
import com.london.data.remote.model.reviews.ReviewResponse
import com.london.data.remote.model.search.SearchTvShowRemote
import com.london.data.remote.model.toprated.TopRatedTvShowRemote
import com.london.data.remote.model.trending.TrendingResponse

interface TvShowRemoteDataSource {

    suspend fun getActorTvShowById(id: Int): Result<ActorTvShowDetailsResponse>
    suspend fun getPopularTvShows(): Result<ApiResponse<PopularTvShowResponse>>
    suspend fun getTrendingTvShows(page: Int): Result<ApiResponse<TrendingResponse>>
    suspend fun deleteTvShowRating(tvShowId: Int, sessionId: String?): Result<RatingRemoteResponse>
    suspend fun getTopRatedTvShows(pageNumber: Int): Result<ApiResponse<TopRatedTvShowRemote>>
    suspend fun getTvShowDetailsById(id: Int): Result<TvShowDetailsRemoteResponse>
    suspend fun getTvSeasonTrailer(tvShowId: Int, seasonNumber: Int): Result<VideoResponse>


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

    suspend fun getTvShowSeasonEpisodes(
        id: Int,
        seasonNumber: Int,
    ): Result<SeasonEpisodesResponse>

    suspend fun getTvShowImagesById(id: Int): Result<ImagesResponse>

    suspend fun getEpisodeDetails(
        tvShowId: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ): Result<EpisodeDetailsResponse>

    suspend fun getEpisodeVideos(
        tvShowId: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ): Result<VideoResponse>

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
