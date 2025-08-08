package com.london.domain.usecase

import com.london.domain.repository.TvShowRepository
import javax.inject.Inject

class GetAccountTvShowStateUseCase @Inject constructor(
    private val tvShowRepository: TvShowRepository
) {
    suspend fun invoke(
        tvShowId: Int,
    ) = tvShowRepository.getAccountTvShowState(
        tvShowId = tvShowId,
    ).rate
}