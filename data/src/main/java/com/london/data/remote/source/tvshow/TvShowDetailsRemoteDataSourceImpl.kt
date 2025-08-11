package com.london.data.remote.source.tvshow

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
import com.london.data.remote.service.details.tvshow.TvShowDetailsApiService
import com.london.data.remote.source.base.BaseRemoteDatasource
import javax.inject.Inject

class TvShowDetailsRemoteDataSourceImpl @Inject constructor(
    private val tvShowDetailsApiService: TvShowDetailsApiService,
) : TvShowDetailsRemoteDataSource, BaseRemoteDatasource {

    override suspend fun getActorTvShowById(id: Int): Result<ActorTvShowDetailsResponse> =
        callApiWithRetry(
            apiCall = { tvShowDetailsApiService.getActorTvShows(actorId = id) },
            mapper = { it }
        )

    override suspend fun getPopularTvShows(): Result<ApiResponse<PopularTvShowResponse>> {
        return callApiWithRetry(
            { tvShowDetailsApiService.getPopularTvShows() },
            mapper = { it }
        )
    }

    override suspend fun getTrendingTvShows(page: Int): Result<ApiResponse<TrendingResponse>> =
        callApiWithRetry(
            apiCall = { tvShowDetailsApiService.getTrendingTvShows(page = page) },
            mapper = { it }
        )

    override suspend fun deleteTvShowRating(
        tvShowId: Int,
        sessionId: String?
    ): Result<RatingRemoteResponse> =callApiWithRetry(
        apiCall = {
            tvShowDetailsApiService.deleteTvShowRating(
                tvShowId = tvShowId,
                sessionId = sessionId
            )
        },
        mapper = { it }
    )

    override suspend fun getAllRatedTvShows(
        accountId: Int,
        sessionId: String
    ): Result<ApiResponse<RatingMediaResponse>> {
        return callApiWithRetry(
            apiCall = {
                tvShowDetailsApiService.getRatedTvShow(
                    accountId = accountId,
                    sessionId = sessionId,
                )
            },
            mapper = { it }
        )
    }

    override suspend fun addTvShowRating(
        tvShowId: Int,
        rating: Double,
        userSessionId: String?,
        guestSessionId: String?
    ): Result<RatingRemoteResponse> {
        return callApiWithRetry(
            apiCall = {
                tvShowDetailsApiService.addTvShowRating(
                    tvShowId = tvShowId,
                    guestSessionId = guestSessionId,
                    userSessionId = userSessionId,
                    ratingRequest = RatingRemoteBody(value = rating.toInt())
                )
            },
            mapper = { it }
        )
    }

    override suspend fun addTvEpisode(
        tvShowId: Int,
        seasonNumber: Int,
        episodeNumber: Int,
        rating: Double,
        userSessionId: String?,
        guestSessionId: String?
    ): Result<RatingRemoteResponse> {
        return callApiWithRetry(
            apiCall = {
                tvShowDetailsApiService.addTvEpisode(
                    tvShowId = tvShowId,
                    seasonNumber = seasonNumber,
                    episodeNumber = episodeNumber,
                    guestSessionId = guestSessionId,
                    userSessionId = userSessionId,
                    ratingRequest = RatingRemoteBody(value = rating.toInt())
                )
            },
            mapper = { it }
        )
    }

    override suspend fun getTvShowReviews(
        tvShowId: Int, pageNumber: Int
    ): Result<ApiResponse<ReviewResponse>> = callApiWithRetry(
        apiCall = { tvShowDetailsApiService.getTvShowReviews(tvShowId = tvShowId, page = pageNumber) },
        mapper = { it }
    )

    override suspend fun getTopRatedTvShows(pageNumber: Int): Result<ApiResponse<TopRatedTvSeriesRemote>> =
        callApi(
            apiCall = { tvShowDetailsApiService.getTopRatedTvShow(pageNumber) },
            mapper = { it }
        )

    override suspend fun getTvShowsByCategoryId(
        categoryId: Int,
        pageNumber: Int,
        includeAdult: Boolean
    ): Result<ApiResponse<SearchTvShowRemote>> = callApiWithRetry(
        apiCall = {
            tvShowDetailsApiService.searchForTvShowsByCategory(
                genreId = categoryId,
                page = pageNumber,
                includeAdult = includeAdult
            )
        },
        mapper = { it }
    )

    override suspend fun getTvShowDetailsById(id: Int): Result<TvShowDetailsRemoteResponse> =
        callApiWithRetry(
            apiCall = { tvShowDetailsApiService.getTvShowDetails(tvShowId = id) },
            mapper = { it }
        )

    override suspend fun getTvShowEpisodesBySeason(
        id: Int,
        seasonNumber: Int
    ): Result<TvShowEpisodesRemoteResponse> =
        callApiWithRetry(
            apiCall = {
                tvShowDetailsApiService.getTvShowEpisodesBySeason(
                    tvShowId = id,
                    seasonNumber = seasonNumber
                )
            },
            mapper = { it }
        )



    override suspend fun getTvShowImagesById(id: Int): Result<TvShowImagesRemoteResponse> =
        callApiWithRetry(
            apiCall = { tvShowDetailsApiService.getTvShowImages(tvShowId = id) },
            mapper = { it }
        )

    override suspend fun getEpisodeDetailsByPosition(
        tvShowId: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ): Result<TvShowEpisodeResponse> =
        callApiWithRetry(
            apiCall = {
                tvShowDetailsApiService.getEpisodeDetails(
                    tvShowId = tvShowId,
                    seasonNumber = seasonNumber,
                    episodeNumber = episodeNumber
                )
            },
            mapper = { it }
        )

    override suspend fun getEpisodeVideos(
        tvShowId: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ): Result<EpisodeVideoResponse> =
        callApiWithRetry(
            apiCall = {
                tvShowDetailsApiService.getEpisodeVideo(
                    seriesId = tvShowId,
                    seasonNumber = seasonNumber,
                    episodeNumber = episodeNumber
                )
            },
            mapper = { it }
        )

    override suspend fun getAccountTvShowStates(
        tvShowId: Int,
        guestSessionId: String?,
        userSessionId: String?,
    ): Result<AccountStatesResponse> = callApiWithRetry(
        apiCall = {
            tvShowDetailsApiService.getAccountTvShowState(
                seriesId = tvShowId,
                guestSessionId = guestSessionId,
                userSessionId = userSessionId
            )
        },
        mapper = { it }
    )

    override suspend fun getAccountTvEpisodeState(
        tvShowId: Int,
        seasonNumber: Int,
        episodeNumber: Int,
        guestSessionId: String?,
        userSessionId: String?
    ): Result<AccountStatesResponse> = callApiWithRetry(
        apiCall = {
            tvShowDetailsApiService.getAccountTvEpisode(
                tvShowId = tvShowId,
                seasonNumber = seasonNumber,
                episodeNumber = episodeNumber,
                guestSessionId = guestSessionId,
                userSessionId = userSessionId
            )
        },
        mapper = { it }
    )

    override suspend fun getTvShowVideos(tvShowId: Int): Result<TvShowVideoResponse> =
        callApiWithRetry(
            apiCall = { tvShowDetailsApiService.getTvShowVideos(tvShowId = tvShowId) },
            mapper = { it }
        )

}
