package com.london.presentation.utils

import com.london.domain.entity.Movie
import com.london.domain.entity.TvShow
import com.london.domain.entity.popular.PopularMedia
import com.london.domain.entity.recent.MediaType
import com.london.domain.entity.toprated.TopRatedMedia
import com.london.presentation.feature.home.HomeUiMedia
import com.london.presentation.feature.home.popular.PopularUiMedia

@JvmName("topRatedMovieToUiMedia")
fun List<TopRatedMedia>.toUiMedia(): List<HomeUiMedia> =
    map { media ->
        HomeUiMedia(
            id = media.id,
            posterUrl = media.posterUrl,
            mediaType = media.mediaType
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

@JvmName("popularMediaToPopularUiMedia")
fun List<PopularMedia>.toPopularUiMedia() =
    map { media ->
        PopularUiMedia(
            id = media.id,
            posterUrl = media.posterUrl,
            mediaType = media.mediaType,
            name = media.name,
            rating = media.rating.toString(),
        )
    }