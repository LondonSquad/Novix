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
    override suspend fun getActorDetailsById(id: Int): Result<ActorDetailsResponse> =
        callApi(
            apiCall = { actorDetailsApiService.getActorDetails(actorId = id) },
            mapper = { it }
        )

    override suspend fun getActorMovieById(id: Int): Result<ActorMovieDetailsResponse> =
        callApi(
            apiCall = { actorDetailsApiService.getActorMovies(actorId = id) },
            mapper = { it }
        )

    override suspend fun getActorTvShowById(id: Int): Result<ActorTvShowDetailsResponse> =
        callApi(
            apiCall = { actorDetailsApiService.getActorTvShows(actorId = id) },
            mapper = { it }
        )

    override suspend fun getActorImagePath(id: Int): Result<ActorImageResponse> =
        callApi(
            apiCall = { actorDetailsApiService.getActorImages(actorId = id) },
            mapper = { it }
        )
}
