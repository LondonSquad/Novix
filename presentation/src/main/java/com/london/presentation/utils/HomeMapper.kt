package com.london.presentation.utils

import com.london.domain.entity.Movie
import com.london.domain.entity.TvShow
import com.london.domain.entity.popular.PopularMedia
import com.london.domain.entity.recent.MediaType
import com.london.domain.entity.toprated.TopRatedMovie
import com.london.domain.entity.toprated.TopRatedTvSeries
import com.london.presentation.feature.home.HomeUiMedia
import com.london.presentation.feature.home.popular.PopularUiMedia

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
fun List<PopularMedia>.toPopularUiMedia() =
    map { movie ->
        PopularUiMedia(
            id = movie.id,
            posterUrl = movie.posterUrl,
            mediaType = MediaType.Movie,
            name = movie.name,
            rating = movie.rating.toString(),
        )
    }
