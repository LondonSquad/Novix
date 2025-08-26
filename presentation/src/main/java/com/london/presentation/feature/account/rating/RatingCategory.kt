package com.london.presentation.feature.account.rating

import androidx.annotation.StringRes
import com.london.presentation.R

enum class RatingCategory(@StringRes val title: Int) {
    All(R.string.all),
    Movies(R.string.movies),
    TvShows(R.string.tv_shows)
}
