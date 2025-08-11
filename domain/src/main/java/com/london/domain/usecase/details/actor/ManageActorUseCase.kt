package com.london.domain.usecase.details.actor

import com.london.domain.entity.Actor
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.actordetails.actorimage.ImageDetails
import com.london.domain.repository.ActorRepository
import com.london.domain.repository.TrendingRepository
import javax.inject.Inject

class ManageActorUseCase @Inject constructor(
    private val actorRepository: ActorRepository,
    private val trendingRepository: TrendingRepository
) {
    suspend fun getActorDetailsById(actorId: Int) = actorRepository.getActorDetailsById(actorId)

    suspend fun getActorImagesById(actorId: Int): List<ImageDetails> =
        actorRepository.getActorImagesById(actorId).profiles

    suspend fun getTrendingActors(page: Int): PagedFetchResponse<Actor> =
        trendingRepository.getTrendingActors(page)
}