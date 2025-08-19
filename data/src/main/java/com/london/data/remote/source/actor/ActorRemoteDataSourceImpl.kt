package com.london.data.remote.source.actor

import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.details.actor.ActorDetailsResponse
import com.london.data.remote.model.details.actor.image.ActorImageResponse
import com.london.data.remote.model.details.movie.cast.MovieCastResponse
import com.london.data.remote.model.details.tvshow.TvShowCastRemoteResponse
import com.london.data.remote.model.trending.TrendingResponse
import com.london.data.remote.service.actor.ActorApiService
import com.london.data.remote.source.base.BaseRemoteDatasource
import javax.inject.Inject

class ActorRemoteDataSourceImpl @Inject constructor(
    private val actorApiService: ActorApiService,
) : ActorRemoteDataSource, BaseRemoteDatasource {

    override suspend fun getActorDetailsById(id: Int): Result<ActorDetailsResponse> = callApiWithRetry(
        apiCall = { actorApiService.getActorDetails(actorId = id) },
        mapper = { it }
    )

    override suspend fun getActorImagePathById(id: Int): Result<ActorImageResponse> = callApiWithRetry(
        apiCall = { actorApiService.getActorImages(actorId = id) },
        mapper = { it }
    )

    override suspend fun getTrendingActors(page: Int): Result<ApiResponse<TrendingResponse>> =
        callApiWithRetry(
            apiCall = { actorApiService.getTrendingActors(page = page) },
            mapper = { it }
        )

    override suspend fun getMovieActors(movieId: Int): Result<MovieCastResponse> = callApiWithRetry(
        apiCall = { actorApiService.getMovieActors(movieId = movieId) },
        mapper = { it })

    override suspend fun getTvShowActors(tvShowId: Int): Result<TvShowCastRemoteResponse> = callApiWithRetry(
        apiCall = { actorApiService.getTvShowActors(tvShowId = tvShowId) },
        mapper = { it }
    )

}
