package com.london.data.remote.source.details.actor

import com.london.data.remote.model.details.actor.model.ActorDetailsResponse
import com.london.data.remote.model.details.actor.model.actorimage.ActorImageResponse
import com.london.data.remote.model.details.actor.model.actormoviedetails.ActorMovieDetailsResponse
import com.london.data.remote.model.details.actor.model.actortvshowdetails.ActorTvShowDetailsResponse
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

    override suspend fun getActorMovieById(id: Int): Result<ActorMovieDetailsResponse> =
        callApiWithRetry(
            apiCall = { actorDetailsApiService.getActorMovies(actorId = id) },
            mapper = { it }
        )

    override suspend fun getActorTvShowById(id: Int): Result<ActorTvShowDetailsResponse> =
        callApiWithRetry(
            apiCall = { actorDetailsApiService.getActorTvShows(actorId = id) },
            mapper = { it }
        )

    override suspend fun getActorImagePath(id: Int): Result<ActorImageResponse> =
        callApiWithRetry(
            apiCall = { actorDetailsApiService.getActorImages(actorId = id) },
            mapper = { it }
        )
}
