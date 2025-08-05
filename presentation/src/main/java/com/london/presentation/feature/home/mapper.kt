package com.london.presentation.feature.home

import com.london.domain.entity.Movie
import com.london.domain.entity.TvShow
import com.london.domain.entity.popular.PopularMovie
import com.london.domain.entity.popular.PopularTvShow
import com.london.domain.entity.recent.MediaType
import com.london.domain.entity.toprated.TopRatedMovie
import com.london.domain.entity.toprated.TopRatedTvSeries
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

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

@JvmName("tvShowToUiMedia")
fun List<TvShow>.toUiMedia(): List<HomeUiMedia> =
    map { show ->
        HomeUiMedia(
            id = show.id,
            posterUrl = show.posterPicture,
            mediaType = MediaType.TvShow
        )
    }

@JvmName("movie")
fun Flow<List<Movie>>.toUiMediaList() = map { it.toUiMedia() }

@JvmName("tvShow")

fun Flow<List<TvShow>>.toUiMediaList() = map { it.toUiMedia() }


@JvmName("topRatedTvSeriesToUiMedia")
fun List<TopRatedTvSeries>.toUiMedia(): List<HomeUiMedia> =
    map { show ->
        HomeUiMedia(
            id = show.id,
            posterUrl = show.posterUrl,
            mediaType = MediaType.TvShow
        )
    }


@JvmName("popularMovieToUiMedia")
fun List<PopularMovie>.toPopularUiMedia() =
    map { movie ->
        PopularUiMedia(
            id = movie.id,
            posterUrl = movie.posterUrl,
            mediaType = MediaType.Movie,
            name = movie.title,
            rating = movie.rating.toString(),
        )
    }

@JvmName("popularTvSeriesToUiMedia")
fun List<PopularTvShow>.toPopularUiMedia() =
    map { show ->
        PopularUiMedia(
            id = show.id,
            posterUrl = show.posterUrl,
            mediaType = MediaType.TvShow,
            name = show.name,
            rating = show.rating.toString(),
        )
    }