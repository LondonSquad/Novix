package com.london.domain.usecase.details.actor

import com.london.domain.entity.Actor
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.repository.ActorRepository
import com.london.domain.repository.MovieRepository
import com.london.domain.repository.TvShowRepository
import javax.inject.Inject

class GetActorUseCase @Inject constructor(
    private val actorRepository: ActorRepository,
    private val movieRepository: MovieRepository,
    private val tvShowRepository: TvShowRepository,
) {
    suspend fun getActorDetailsById(id: Int) = actorRepository.getActorDetailsById(id)

    suspend fun getActorImagesById(id: Int): List<String> =
        actorRepository.getActorImagesById(id).imageUrl

    suspend fun getTrendingActors(page: Int): PagedFetchResponse<Actor> =
        actorRepository.getTrendingActors(page)

    suspend fun getActorTvShowPicksById(id: Int) =
        tvShowRepository.getActorTvShowById(id)

    suspend fun getActorMoviePicksById(id: Int) =
        movieRepository.getActorMoviePicksById(id)
}