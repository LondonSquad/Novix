package com.london.domain.usecase.rating

import com.london.domain.entity.myrating.AllRatedContent
import com.london.domain.repository.myrating.MyRatingRepository
import javax.inject.Inject

class GetAllRatedUseCase @Inject constructor(
    private val repository: MyRatingRepository
) {
    suspend fun invoke(): AllRatedContent {
        val ratedMedia = repository.getAllRatedMedia().shuffled()
        return AllRatedContent(items = ratedMedia)
    }
}