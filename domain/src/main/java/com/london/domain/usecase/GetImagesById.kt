package com.london.domain.usecase

import com.london.domain.entity.tvshowdetails.ImageItemEntity
import com.london.domain.repository.DetailsRepository

class GetImagesById(
    private val detailsRepository: DetailsRepository
) {
    suspend operator fun invoke(tvShowId: Int): List<ImageItemEntity> {
        val images = detailsRepository.getImagesTvShowById(tvShowId)

        return when {
            images.backdrops.isNotEmpty() -> images.backdrops.take(10)
            images.posters.isNotEmpty() -> images.posters.take(10)
            images.logos.isNotEmpty() -> images.logos.take(10)
            else -> emptyList()
        }
    }
}