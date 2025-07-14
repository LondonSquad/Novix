package com.london.data.mapper

import com.london.data.datasource.remote.details.tvshowdetails.model.ImageItem
import com.london.data.datasource.remote.details.tvshowdetails.model.TvShowImagesRemoteResponse
import com.london.domain.entity.ImageItemEntity
import com.london.domain.entity.TvShowImagesEntity

object TvShowImagesMapper {

    fun TvShowImagesRemoteResponse.toEntity(): TvShowImagesEntity {
        return TvShowImagesEntity(
            backdrops = backdrops.map { it.toEntity() },
            id = id,
            logos = logos.map { it.toEntity() },
            posters = posters.map { it.toEntity() }
        )
    }

    private fun ImageItem.toEntity(): ImageItemEntity {
        return ImageItemEntity(
            aspectRatio = aspectRatio,
            height = height,
            iso6391 = iso6391,
            filePath = "https://image.tmdb.org/t/p/w500${this.filePath}",
            voteAverage = voteAverage,
            voteCount = voteCount,
            width = width
        )
    }

    fun List<ImageItem>.toEntityList(): List<ImageItemEntity> {
        return map { it.toEntity() }
    }
}