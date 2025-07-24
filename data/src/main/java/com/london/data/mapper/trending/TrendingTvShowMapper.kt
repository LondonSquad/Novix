package com.london.data.mapper.trending

import com.london.data.datasource.remote.home.trending.model.TrendingTvShowResponse
import com.london.domain.entity.trending.TrendingTvShow
import com.london.data.utils.orZero
import com.london.data.utils.asImageUrlOrEmpty

fun TrendingTvShowResponse.toTrendingTvShow(): TrendingTvShow = TrendingTvShow(
    id = id.orZero(),
    posterPath = posterPath.asImageUrlOrEmpty(),
    genreIds = genreIds ?: emptyList()
) 