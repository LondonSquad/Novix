package com.london.presentation.utils

import androidx.compose.runtime.Composable
import com.london.presentation.R
import com.london.presentation.feature.search.SearchCategory

@Composable
fun <T> ResultOrEmpty(
    items: List<T>,
    otherItems: List<T>? = null,
    emptyContent: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    if (items.isEmpty() && otherItems.isNullOrEmpty()) {
        emptyContent()
    } else {
        content()
    }
}

fun convertGenreCodeToString(genreId: Int, searchCategory: SearchCategory): Int {
    return when (searchCategory) {
        SearchCategory.Movies -> {
            when (genreId) {
                28 -> R.string.action
                12 ->  R.string.adventure
                16 ->  R.string.animation
                35 ->  R.string.comedy
                80 ->  R.string.crime
                99 ->  R.string.documentary
                18 ->  R.string.drama
                10751 ->  R.string.family
                14 ->  R.string.fantasy
                36 ->  R.string.history
                27 ->  R.string.horror
                10402 ->  R.string.music
                9648 ->  R.string.mystery
                10749 ->  R.string.romance
                878 ->  R.string.sci_fi
                10770 ->  R.string.tv_movie
                53 ->  R.string.thriller
                10752 ->  R.string.war
                37 ->  R.string.western
                else ->  R.string.unknown
            }
        }
        SearchCategory.TvShows -> {
            when (genreId) {
                10759 ->  R.string.action
                16 ->  R.string.animation
                35 ->  R.string.comedy
                80 ->  R.string.crime
                99 ->  R.string.documentary
                18 ->  R.string.drama
                10751 ->  R.string.family
                10762 ->  R.string.kids
                9648 ->  R.string.mystery
                10763 ->  R.string.news
                10764 ->  R.string.reality
                10765 ->  R.string.sci_fi
                10766 ->  R.string.soap
                10767 ->  R.string.talk
                10768 ->  R.string.war
                37 ->  R.string.western
                else ->  R.string.action
            }
        }
        SearchCategory.Actors ->  R.string.unknown
    }
}