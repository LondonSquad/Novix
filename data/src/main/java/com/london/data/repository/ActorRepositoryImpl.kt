package com.london.data.repository

import com.london.data.datasource.remote.details.actordetails.ActorDetailsRemoteDataSource
import com.london.data.mapper.actordetails.toEntity
import com.london.domain.ActorDetailsSearchFailedException
import com.london.domain.GetCastByIdFailedException
import com.london.domain.GetImagesByIdFailedException
import com.london.domain.entity.actordetails.ActorDetails
import com.london.domain.entity.actordetails.actorimage.ActorImageDetails
import com.london.domain.entity.actordetails.actormovie.ActorMovieDetails
import com.london.domain.entity.actordetails.actortvshow.ActorTvShowDetails
import com.london.domain.repository.ActorRepository

class ActorRepositoryImpl(
    private val repository: ActorDetailsRemoteDataSource

): ActorRepository {
    override suspend fun getActorDetailsById(actorId: Int): ActorDetails {
        return runCatching {
            repository.getActorDetailsById(actorId).toEntity()
        }.getOrElse {
            throw ActorDetailsSearchFailedException()
        }
    }

    override suspend fun getActorMoviePicksById(actorId: Int): ActorMovieDetails {
        return runCatching {
            repository.getActorMovieById(actorId).toEntity()
        }.getOrElse {
            throw GetCastByIdFailedException()
        }
    }

    override suspend fun getActorTvShowPicksById(actorId: Int): ActorTvShowDetails {
        return runCatching {
            repository.getActorTvShowById(actorId).toEntity()
        }.getOrElse {
            throw GetCastByIdFailedException()
        }
    }

    override suspend fun getActorImagesById(actorId: Int): ActorImageDetails {
        return runCatching {
            repository.getActorImagePath(actorId).toEntity()
        }.getOrElse {
            throw GetImagesByIdFailedException()
        }
    }
}