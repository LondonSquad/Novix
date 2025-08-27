package com.london.presentation.feature.search

import androidx.annotation.StringRes
import com.london.presentation.R

enum class SearchCategory(@StringRes val title: Int) {
    Movies(R.string.movies),
    TvShows(R.string.tv_shows),
    Actors(R.string.actors),
}
