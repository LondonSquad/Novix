package com.london.data.datasource.remote.details.actordetails

import com.london.data.datasource.remote.BaseRemoteDatasource
import com.london.data.datasource.remote.details.actordetails.api.ActorDetailsApiService
import com.london.data.datasource.remote.details.actordetails.model.ActorDetailsResponse
import com.london.data.datasource.remote.details.actordetails.model.actorimage.ActorImageResponse
import com.london.data.datasource.remote.details.actordetails.model.actormoviedetails.ActorMovieDetailsResponse
import com.london.data.datasource.remote.details.actordetails.model.actortvshowdetails.ActorTvShowDetailsResponse
import org.koin.core.annotation.Single

@Single
class ActorDetailsRemoteDataSourceImpl(
    private val actorDetailsApiService: ActorDetailsApiService,
) : ActorDetailsRemoteDataSource, BaseRemoteDatasource {
    override suspend fun getActorDetailsById(actorId: Int): Result<ActorDetailsResponse> =
        callApi(
            apiCall = { actorDetailsApiService.getActorDetails(actorId = actorId) },
            mapper = { it }
        )

    override suspend fun getActorMovieById(actorId: Int): Result<ActorMovieDetailsResponse> =
        callApi(
            apiCall = { actorDetailsApiService.getActorMovies(actorId = actorId) },
            mapper = { it }
        )

    override suspend fun getActorTvShowById(actorId: Int): Result<ActorTvShowDetailsResponse> =
        callApi(
            apiCall = { actorDetailsApiService.getActorTvShows(actorId = actorId) },
            mapper = { it }
        )

    override suspend fun getActorImagePath(actorId: Int): Result<ActorImageResponse> =
        callApi(
            apiCall = { actorDetailsApiService.getActorImages(actorId = actorId) },
            mapper = { it }
        )
}
