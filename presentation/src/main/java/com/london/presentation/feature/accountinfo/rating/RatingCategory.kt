package com.london.presentation.feature.accountinfo.rating

import androidx.annotation.StringRes
import com.london.presentation.R

enum class RatingCategory(@StringRes val title: Int) {
    All(R.string.all),
    Movies(R.string.Movies),
    TvShows(R.string.TV_Shows)
}