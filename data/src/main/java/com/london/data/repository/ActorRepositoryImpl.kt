package com.london.data.repository

import com.london.data.mapper.actordetails.toEntity
import com.london.data.remote.source.details.actor.ActorDetailsRemoteDataSource
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
    override suspend fun getActorDetailsById(id: Int): ActorDetails =
        dataSource.getActorDetailsById(id).getOrThrow().toEntity()


    override suspend fun getActorMoviePicksById(id: Int): ActorMovieDetails =
        dataSource.getActorMovieById(id).getOrThrow().toEntity()


    override suspend fun getActorTvShowPicksById(id: Int): ActorTvShowDetails =
        dataSource.getActorTvShowById(id).getOrThrow().toEntity()

    override suspend fun getActorImagesById(id: Int): ActorImageDetails =
        dataSource.getActorImagePath(id).getOrThrow().toEntity()
}
