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
        id: Int
    ): ActorDetailsResponse =
        actorDetailsApiService.getActorDetails(
            actorId = id
        )

    override suspend fun getActorMovieById(
        id: Int
    ): ActorMovieDetailsResponse =
        actorDetailsApiService.getActorMovies(
            actorId = id
        )

    override suspend fun getActorTvShowById(
        id: Int
    ): ActorTvShowDetailsResponse =
        actorDetailsApiService.getActorTvShows(
            actorId = id
        )

    override suspend fun getActorImagePath(
        id: Int
    ): ActorImageResponse =
        actorDetailsApiService.getActorImages(
            actorId = id
        )
}