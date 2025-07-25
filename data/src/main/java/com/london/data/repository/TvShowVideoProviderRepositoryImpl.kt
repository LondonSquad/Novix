package com.london.data.repository

import com.london.data.datasource.remote.details.videoprovider.tvshow.TvShowVideoProviderRemote
import com.london.data.mapper.videoprovider.tvshow.toTvShowVideo
import com.london.domain.entity.videoprovider.TvShowVideo
import com.london.domain.repository.TvShowVideoProviderRepository
import org.koin.core.annotation.Single

@Single
class TvShowVideoProviderRepositoryImpl(
    private val tvShowVideoProviderRemote: TvShowVideoProviderRemote
) : TvShowVideoProviderRepository {
    override suspend fun getTvShowVideos(tvShowId: Int): List<TvShowVideo> =
        tvShowVideoProviderRemote.getTvShowVideos(tvShowId).getOrThrow().tvShow?.map {
            it.toTvShowVideo()
        }.orEmpty()
}
