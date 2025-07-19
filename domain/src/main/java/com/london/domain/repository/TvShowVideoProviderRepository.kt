package com.london.domain.repository

import com.london.domain.entity.videoprovider.TvShowVideo

interface TvShowVideoProviderRepository {
suspend fun getTvShowVideos(tvShowId: Int): List<TvShowVideo>
}