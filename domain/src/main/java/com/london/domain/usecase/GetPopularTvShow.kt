package com.london.domain.usecase

import com.london.domain.repository.PopularRepository
import javax.inject.Inject

class GetPopularTvShow @Inject constructor(
    private val popularRepository: PopularRepository
) {
    suspend fun invoke(limit: Int = LIMIT) = popularRepository.getPopularTvShows().take(limit)

    companion object{
        const val LIMIT = 5
    }
}