package com.london.data.mapper.details.tvshow

import com.london.data.remote.model.details.tvshow.TvShowImagesRemoteResponse
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.orZero
import com.london.domain.entity.tvshowdetails.TvShowImagesEntity

object TvShowImagesMapper {
    fun TvShowImagesRemoteResponse.toEntity(): TvShowImagesEntity = TvShowImagesEntity(
        backdropsUrl = backdrops.orEmpty().map { it.filePath.asImageUrlOrEmpty() },
        id = id.orZero(),
        logosUrl = logos.orEmpty().map { it.filePath.asImageUrlOrEmpty() },
        postersUrl = posters.orEmpty().map { it.filePath.asImageUrlOrEmpty() }
    )
}