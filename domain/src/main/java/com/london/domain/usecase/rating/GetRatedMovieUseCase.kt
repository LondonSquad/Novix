package com.london.domain.usecase.rating

import com.london.domain.entity.myrating.RatedMedia
import com.london.domain.repository.myrating.MyRatingRepository
import javax.inject.Inject

class GetRatedMediaUseCase @Inject constructor(
    private val repository: MyRatingRepository
) {
    suspend fun invoke(): List<RatedMedia> = repository.getAllRatedMedia()
}