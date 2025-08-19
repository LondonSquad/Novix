package com.london.presentation.shared.container

import androidx.compose.foundation.clickable
import androidx.compose.ui.Modifier
import com.london.domain.entity.movie.Movie
import com.london.domain.entity.tvshow.TvShow

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

fun <T : Any> Modifier.navigationClickable(
    item: T,
    onItemClick: ((T) -> Unit)? = null,
    onNavigateToMovie: (Int) -> Unit = {},
    onNavigateToTvShow: (Int) -> Unit = {}
): Modifier = this.clickable {
    onItemClick?.invoke(item) ?: when (item) {
        is Movie -> onNavigateToMovie(item.id)
        is TvShow -> onNavigateToTvShow(item.id)
        else -> Unit
    }
}
