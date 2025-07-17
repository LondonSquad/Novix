package com.london.data.datasource.remote.details.actordetails

import com.london.data.datasource.remote.ApiConstants
import com.london.data.datasource.remote.details.actordetails.model.ActorDetailsResponse
import com.london.data.datasource.remote.details.actordetails.model.actorimage.ActorImageResponse
import com.london.data.datasource.remote.details.actordetails.model.actormoviedetails.ActorMovieDetailsResponse
import com.london.data.datasource.remote.details.actordetails.model.actortvshowdetails.ActorTvShowDetailsResponse
import com.london.data.utils.get
import io.ktor.client.HttpClient
import org.koin.core.annotation.Single


@Single
class ActorDetailsRemoteDataSourceImpl(
    private val ktorClient: HttpClient,
) : ActorDetailsRemoteDataSource {

    override suspend fun getActorDetailsById(actorId: Int): ActorDetailsResponse =
        ktorClient.get(path = ApiConstants.getActorDetailsPath(actorId))

    override suspend fun getActorMovieById(actorId: Int): ActorMovieDetailsResponse =
        ktorClient.get(path = ApiConstants.getActorMoviesPath(actorId))

    override suspend fun getActorTvShowById(actorId: Int): ActorTvShowDetailsResponse =
        ktorClient.get(path = ApiConstants.getActorTvShowsPath(actorId))

    override suspend fun getActorImagePath(actorId: Int): ActorImageResponse =
        ktorClient.get(path = ApiConstants.getActorImagePath(actorId))
}
