package com.london.presentation.feature.home

import com.london.domain.entity.recent.MediaType
import com.london.domain.entity.toprated.TopRatedMovie
import com.london.domain.entity.toprated.TopRatedTvSeries

@JvmName("movieToUiMedia")
fun List<TopRatedMovie>.toUiMedia(): List<TopRatedUiMedia> =
    map { movie ->
        TopRatedUiMedia(
            id = movie.id,
            posterUrl = movie.posterUrl,
            mediaType = MediaType.Movie
        )
    }

@JvmName("tvSeriesToUiMedia")
fun List<TopRatedTvSeries>.toUiMedia(): List<TopRatedUiMedia> =
    map { show ->
        TopRatedUiMedia(
            id = show.id,
            posterUrl = show.posterUrl,
            mediaType = MediaType.TvShow
        )
    }