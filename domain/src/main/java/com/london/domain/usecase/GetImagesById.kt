package com.london.domain.usecase

import com.london.domain.KoverIgnore
import com.london.domain.entity.tvshowdetails.ImageItemEntity
import com.london.domain.repository.TvShowRepository
import javax.inject.Inject

@KoverIgnore
class GetImagesById @Inject constructor(
    private val tvShowRepository: TvShowRepository
) {
    suspend operator fun invoke(tvShowId: Int): List<ImageItemEntity> {
        val images = tvShowRepository.getImagesTvShowById(tvShowId)

        return when {
            images.backdrops.isNotEmpty() -> images.backdrops.take(10)
            images.posters.isNotEmpty() -> images.posters.take(10)
            images.logos.isNotEmpty() -> images.logos.take(10)
            else -> emptyList()
        }
    }
}