package com.london.presentation.feature.category.main

import androidx.annotation.StringRes
import com.london.presentation.R

enum class MediaCategory(@StringRes val title: Int) {
    Movies(R.string.Movies),
    TvShows(R.string.TV_Shows),
}
