package com.london.presentation.features.search

import androidx.annotation.StringRes
import com.london.presentation.R

enum class SearchCategory(@StringRes val title: Int) {
    Movies(R.string.Movies),
    TvShows(R.string.TV_Shows),
    Actors(R.string.Actors)
}