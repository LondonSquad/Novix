package com.london.domain.usecase

import com.london.domain.repository.ActorRepository
import javax.inject.Inject

class GetCastById @Inject constructor(
    private val repository: ActorRepository
) {
    suspend operator fun invoke(tvShowId: Int) = repository.getCastTvShowById(tvShowId)
}