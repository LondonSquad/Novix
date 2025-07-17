package com.london.data.datasource.remote.details.actordetails

import com.london.data.datasource.remote.ApiConstants
import com.london.data.datasource.remote.details.actordetails.model.ActorDetailsResponse
import com.london.data.datasource.remote.details.actordetails.model.actorimage.ActorImageResponse
import com.london.data.datasource.remote.details.actordetails.model.actormoviedetails.ActorMovieDetailsResponse
import com.london.data.datasource.remote.details.actordetails.model.actortvshowdetails.ActorTvShowDetailsResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.path

class ActorDetailsRemoteDataSourceImpl(
    private val ktorClient: HttpClient,
) : ActorDetailsRemoteDataSource {
    override suspend fun getActorDetailsById(
        actorId: Int
    ): ActorDetailsResponse {
        val response = ktorClient.get {
            url {
                path(ApiConstants.getActorDetailsPath(actorId))
            }
        }
        return response.body()
    }

    override suspend fun getActorMovieById(actorId: Int): ActorMovieDetailsResponse {

        val response = ktorClient.get {
            url {
                path(ApiConstants.getActorMoviesPath(actorId))
            }
        }
        return response.body()
    }

    override suspend fun getActorTvShowById(actorId: Int): ActorTvShowDetailsResponse {

        val response = ktorClient.get {
            url {
                path(ApiConstants.getActorTvShowsPath(actorId))
            }
        }
        return response.body()
    }

    override suspend fun getActorImagePath(actorId: Int): ActorImageResponse {

        val response = ktorClient.get {
            url {
                path(ApiConstants.getActorImagePath(actorId))
            }
        }
        return response.body()
    }
}
