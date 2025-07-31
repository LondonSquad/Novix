package com.london.presentation.feature.home

import com.london.domain.entity.Movie
import com.london.domain.entity.TvShow
import com.london.domain.entity.recent.MediaType
import com.london.domain.entity.toprated.TopRatedMovie
import com.london.domain.entity.toprated.TopRatedTvSeries

@JvmName("topRatedMovieToUiMedia")
fun List<TopRatedMovie>.toUiMedia(): List<HomeUiMedia> =
    map { movie ->
        HomeUiMedia(
            id = movie.id,
            posterUrl = movie.posterUrl,
            mediaType = MediaType.Movie
        )
    }

@JvmName("movieToUiMedia")
fun List<Movie>.toUiMedia(): List<HomeUiMedia> =
    map { movie ->
        HomeUiMedia(
            id = movie.id,
            posterUrl = movie.posterUrl,
            mediaType = MediaType.Movie
        )
    }

@JvmName("topRatedTvSeriesToUiMedia")
fun List<TopRatedTvSeries>.toUiMedia(): List<HomeUiMedia> =
    map { show ->
        HomeUiMedia(
            id = show.id,
            posterUrl = show.posterUrl,
            mediaType = MediaType.TvShow
        )
    }

@JvmName("tvShowToUiMedia")
fun List<TvShow>.toUiMedia(): List<HomeUiMedia> =
    map { show ->
        HomeUiMedia(
            id = show.id,
            posterUrl = show.posterPicture,
            mediaType = MediaType.TvShow
        )
    }