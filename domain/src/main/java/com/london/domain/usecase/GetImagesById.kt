package com.london.domain.usecase

import com.london.domain.KoverIgnore
import com.london.domain.entity.tvshowdetails.ImageItemEntity
import com.london.domain.repository.DetailsRepository
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single

@Single
@KoverIgnore
class GetImagesById(
    @Provided
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