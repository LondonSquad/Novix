package com.london.data.remote.source.actor

import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.details.actor.model.ActorDetailsResponse
import com.london.data.remote.model.details.actor.model.actorimage.ActorImageResponse
import com.london.data.remote.model.details.movie.model.moviecast.MovieCastResponse
import com.london.data.remote.model.details.tvshow.model.TvShowCastRemoteResponse
import com.london.data.remote.model.home.trending.TrendingResponse
import com.london.data.remote.service.details.actor.ActorDetailsApiService
import com.london.data.remote.source.base.BaseRemoteDatasource
import javax.inject.Inject

class ActorDetailsRemoteDataSourceImpl @Inject constructor(
    private val actorDetailsApiService: ActorDetailsApiService,
) : ActorDetailsRemoteDataSource, BaseRemoteDatasource {
    override suspend fun getActorDetailsById(id: Int): Result<ActorDetailsResponse> =
        callApiWithRetry(
            apiCall = { actorDetailsApiService.getActorDetails(actorId = id) },
            mapper = { it }
        )

    override suspend fun getActorImagePath(id: Int): Result<ActorImageResponse> =
        callApiWithRetry(
            apiCall = { actorDetailsApiService.getActorImages(actorId = id) },
            mapper = { it }
        )

    override suspend fun getTrendingActors(page: Int): Result<ApiResponse<TrendingResponse>> =
        callApiWithRetry(
            apiCall = { actorDetailsApiService.getTrendingActors(page = page) },
            mapper = { it }
        )

    override suspend fun getMovieCast(movieId: Int): Result<MovieCastResponse> {
        return callApiWithRetry(
            apiCall = { actorDetailsApiService.getMovieCast(movieId = movieId) },
            mapper = { it })
    }

    override suspend fun getCastsByTvShowId(id: Int): Result<TvShowCastRemoteResponse> =
        callApiWithRetry(
            apiCall = { actorDetailsApiService.getTvShowCast(tvShowId = id) },
            mapper = { it }
        )
}
