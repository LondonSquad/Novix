@file:KoverIgnore
package com.london.data.mapper.details.tvshow

import com.london.data.remote.model.details.tvshow.model.ImageItem
import com.london.data.remote.model.details.tvshow.model.TvShowImagesRemoteResponse
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.orZero
import com.london.data.utils.roundToDecimal
import com.london.domain.KoverIgnore
import com.london.domain.entity.tvshowdetails.ImageItemEntity
import com.london.domain.entity.tvshowdetails.TvShowImagesEntity

object TvShowImagesMapper {

    fun TvShowImagesRemoteResponse.toEntity(): TvShowImagesEntity {
        return TvShowImagesEntity(
            backdrops = backdrops?.map { it.toEntity() }.orEmpty(),
            id = id.orZero(),
            logos = logos?.map { it.toEntity() }.orEmpty(),
            posters = posters?.map { it.toEntity() }.orEmpty()
        )
    }

    private fun ImageItem.toEntity(): ImageItemEntity {
        return ImageItemEntity(
            aspectRatio = aspectRatio,
            height = height,
            iso6391 = iso6391,
            fileUrl = filePath.asImageUrlOrEmpty(),
            voteAverage = voteAverage.roundToDecimal(),
            voteCount = voteCount,
            width = width
        )
    }

    fun List<ImageItem>.toEntityList(): List<ImageItemEntity> {
        return map { it.toEntity() }
    }
}