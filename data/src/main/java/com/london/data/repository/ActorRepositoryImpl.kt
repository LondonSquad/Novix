package com.london.data.repository

import com.london.data.datasource.remote.details.actordetails.ActorDetailsRemoteDataSource
import com.london.data.mapper.actordetails.toEntity
import com.london.domain.entity.actordetails.ActorDetails
import com.london.domain.entity.actordetails.actorimage.ActorImageDetails
import com.london.domain.entity.actordetails.actormovie.ActorMovieDetails
import com.london.domain.entity.actordetails.actortvshow.ActorTvShowDetails
import com.london.domain.repository.ActorRepository
import org.koin.core.annotation.Single

@Single
class ActorRepositoryImpl(
    private val dataSource: ActorDetailsRemoteDataSource
) : ActorRepository {
    override suspend fun getActorDetailsById(actorId: Int): ActorDetails =
        dataSource.getActorDetailsById(actorId).getOrThrow().toEntity()


    override suspend fun getActorMoviePicksById(actorId: Int): ActorMovieDetails =
        dataSource.getActorMovieById(actorId).getOrThrow().toEntity()


    override suspend fun getActorTvShowPicksById(actorId: Int): ActorTvShowDetails =
        dataSource.getActorTvShowById(actorId).getOrThrow().toEntity()

    override suspend fun getActorImagesById(actorId: Int): ActorImageDetails =
        dataSource.getActorImagePath(actorId).getOrThrow().toEntity()
}
