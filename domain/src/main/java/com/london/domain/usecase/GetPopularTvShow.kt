package com.london.domain.usecase

import com.london.domain.repository.PopularRepository
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single

@Single
class GetPopularTvShow(
    @Provided
    private val popularRepository: PopularRepository
) {
    suspend fun invoke(limit: Int = LIMIT) = popularRepository.getPopularTvShows().take(limit)

    companion object{
        const val LIMIT = 5
    }
}