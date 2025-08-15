package com.london.presentation.shared.container

import com.london.domain.entity.Movie
import com.london.domain.entity.TvShow

fun <T : Any> T.getName(): String {
    return when (this) {
        is Movie -> this.name
        is TvShow -> this.name
        else -> toString()
    }
}

fun <T : Any> T.getImageUrl(): String? {
    return when (this) {
        is Movie -> this.posterUrl
        is TvShow -> this.posterPicture
        else -> null
    }
}

fun <T : Any> filterItemsByCategory(
    items: List<T>,
    config: MediaGridConfig
): List<T> {
    return when {
        config.isMovieSelected -> items.filter { it is Movie }
        config.isTvShowSelected -> items.filter { it is TvShow }
        else -> items
    }
}
