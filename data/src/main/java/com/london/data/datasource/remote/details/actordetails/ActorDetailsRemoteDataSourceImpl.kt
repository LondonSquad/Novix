package com.london.data.datasource.remote.details.actordetails

import com.london.data.datasource.remote.details.actordetails.api.ActorDetailsApiService
import com.london.data.datasource.remote.details.actordetails.model.ActorDetailsResponse
import com.london.data.datasource.remote.details.actordetails.model.actorimage.ActorImageResponse
import com.london.data.datasource.remote.details.actordetails.model.actormoviedetails.ActorMovieDetailsResponse
import com.london.data.datasource.remote.details.actordetails.model.actortvshowdetails.ActorTvShowDetailsResponse
import org.koin.core.annotation.Single
@Single
class ActorDetailsRemoteDataSourceImpl(
    private val actorDetailsApiService: ActorDetailsApiService,
) : ActorDetailsRemoteDataSource {

    override suspend fun getActorDetailsById(
        actorId: Int
    ): ActorDetailsResponse =
        actorDetailsApiService.getActorDetails(
            actorId = actorId
        )

    override suspend fun getActorMovieById(
        actorId: Int
    ): ActorMovieDetailsResponse =
        actorDetailsApiService.getActorMovies(
            actorId = actorId
        )

    override suspend fun getActorTvShowById(
        actorId: Int
    ): ActorTvShowDetailsResponse =
        actorDetailsApiService.getActorTvShows(
            actorId = actorId
        )

    override suspend fun getActorImagePath(
        actorId: Int
    ): ActorImageResponse =
        actorDetailsApiService.getActorImages(
            actorId = actorId
        )
}