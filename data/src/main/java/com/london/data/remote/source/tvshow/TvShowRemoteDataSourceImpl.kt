package com.london.data.remote.source.tvshow

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
import com.london.data.remote.service.tvshow.TvShowApiService
import com.london.data.remote.source.base.BaseRemoteDatasource
import javax.inject.Inject

class TvShowRemoteDataSourceImpl @Inject constructor(
    private val tvShowApiService: TvShowApiService,
) : TvShowRemoteDataSource, BaseRemoteDatasource {

    override suspend fun getActorTvShowById(id: Int): Result<ActorTvShowDetailsResponse> =
        callApiWithRetry(
            apiCall = { tvShowApiService.getActorTvShows(actorId = id) },
            mapper = { it }
        )

    override suspend fun getPopularTvShows(): Result<ApiResponse<PopularTvShowResponse>> {
        return callApiWithRetry(
            { tvShowApiService.getPopularTvShows() },
            mapper = { it }
        )
    }

    override suspend fun getTrendingTvShows(page: Int): Result<ApiResponse<TrendingResponse>> =
        callApiWithRetry(
            apiCall = { tvShowApiService.getTrendingTvShows(page = page) },
            mapper = { it }
        )

    override suspend fun deleteTvShowRating(
        tvShowId: Int,
        sessionId: String?
    ): Result<RatingRemoteResponse> = callApiWithRetry(
        apiCall = {
            tvShowApiService.deleteTvShowRating(
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
                tvShowApiService.getRatedTvShows(
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
                tvShowApiService.addTvShowRating(
                    tvShowId = tvShowId,
                    guestSessionId = guestSessionId,
                    userSessionId = userSessionId,
                    ratingRequest = RatingRemoteBody(value = rating.toInt())
                )
            },
            mapper = { it }
        )
    }

    override suspend fun addTvShowEpisode(
        tvShowId: Int,
        seasonNumber: Int,
        episodeNumber: Int,
        rating: Double,
        userSessionId: String?,
        guestSessionId: String?
    ): Result<RatingRemoteResponse> {
        return callApiWithRetry(
            apiCall = {
                tvShowApiService.addTvEpisode(
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
        apiCall = { tvShowApiService.getTvShowReviews(tvShowId = tvShowId, page = pageNumber) },
        mapper = { it }
    )

    override suspend fun getTopRatedTvShows(pageNumber: Int): Result<ApiResponse<TopRatedTvSeriesRemote>> =
        callApi(
            apiCall = { tvShowApiService.getTopRatedTvShows(pageNumber) },
            mapper = { it }
        )

    override suspend fun getTvShowsByCategoryId(
        categoryId: Int,
        pageNumber: Int,
        includeAdult: Boolean
    ): Result<ApiResponse<SearchTvShowRemote>> = callApiWithRetry(
        apiCall = {
            tvShowApiService.getTvShowByCategory(
                genreId = categoryId,
                page = pageNumber,
                includeAdult = includeAdult
            )
        },
        mapper = { it }
    )

    override suspend fun getTvShowDetailsById(id: Int): Result<TvShowDetailsRemoteResponse> =
        callApiWithRetry(
            apiCall = { tvShowApiService.getTvShowDetails(tvShowId = id) },
            mapper = { it }
        )

    override suspend fun getTvShowEpisodesBySeason(
        id: Int,
        seasonNumber: Int
    ): Result<TvShowEpisodesRemoteResponse> =
        callApiWithRetry(
            apiCall = {
                tvShowApiService.getTvShowEpisodesBySeason(
                    tvShowId = id,
                    seasonNumber = seasonNumber
                )
            },
            mapper = { it }
        )

    override suspend fun getTvShowImagesById(id: Int): Result<ImagesResponse> =
        callApiWithRetry(
            apiCall = { tvShowApiService.getTvShowImages(tvShowId = id) },
            mapper = { it }
        )

    override suspend fun getEpisodeDetails(
        tvShowId: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ): Result<TvShowEpisodeResponse> =
        callApiWithRetry(
            apiCall = {
                tvShowApiService.getEpisodeDetails(
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
    ): Result<VideoResponse> =
        callApiWithRetry(
            apiCall = {
                tvShowApiService.getEpisodeVideo(
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
            tvShowApiService.getAccountTvShowState(
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
            tvShowApiService.getAccountTvEpisode(
                tvShowId = tvShowId,
                seasonNumber = seasonNumber,
                episodeNumber = episodeNumber,
                guestSessionId = guestSessionId,
                userSessionId = userSessionId
            )
        },
        mapper = { it }
    )

    override suspend fun getTvSeasonTrailer(
        tvShowId: Int,
        seasonNumber: Int
    ): Result<VideoResponse> =
        callApiWithRetry(
            apiCall = {
                tvShowApiService.getTvSeasonTrailer(
                    tvShowId = tvShowId,
                    seasonNumber = seasonNumber
                )
            },
            mapper = { it }
        )
}
