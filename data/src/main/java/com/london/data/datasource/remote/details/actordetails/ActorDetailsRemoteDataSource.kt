package com.london.data.datasource.remote.details.actordetails

import com.london.data.datasource.remote.details.actordetails.model.ActorDetailsResponse
import com.london.data.datasource.remote.details.actordetails.model.actorimage.ActorImageResponse
import com.london.data.datasource.remote.details.actordetails.model.actormoviedetails.ActorMovieDetailsResponse
import com.london.data.datasource.remote.details.actordetails.model.actortvshowdetails.ActorTvShowDetailsResponse

interface ActorDetailsRemoteDataSource {
    suspend fun getActorDetailsById(actorId: Int): ActorDetailsResponse
    suspend fun getActorMovieById(actorId: Int): ActorMovieDetailsResponse
    suspend fun getActorTvShowById(actorId: Int): ActorTvShowDetailsResponse
    suspend fun getActorImagePath(actorId: Int): ActorImageResponse
}